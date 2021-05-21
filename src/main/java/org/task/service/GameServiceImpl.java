package org.task.service;


import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.task.data.entity.CommentEntity;
import org.task.data.entity.GameEntity;
import org.task.data.entity.PlayerEntity;
import org.task.data.repository.CommentRepository;
import org.task.data.repository.GameRepository;
import org.task.data.repository.PlayerRepository;
import org.task.exceptions.EntityNotFoundException;
import org.task.model.nbaconsumer.NBAGame;
import org.task.model.nbaconsumer.NBAGamePage;
import org.task.model.nbaconsumer.NBAGameStatPage;
import org.task.model.nbaconsumer.NBAStat;
import org.task.model.nbaconsumer.NBAStatPlayer;
import org.task.model.producer.AddComment;
import org.task.model.producer.Comment;
import org.task.model.producer.Game;
import org.task.model.producer.Player;

@Slf4j
@AllArgsConstructor
@Service
public class GameServiceImpl implements GameService {

  private final GameRepository gameRepository;
  private final PlayerRepository playerRepository;
  private final CommentRepository commentRepository;
  private final NBAClient nbaClient;

  @Transactional
  public Game getGameById(final String id) {
    return gameRepository.findById(id).map(gameEntity -> this.toGame(gameEntity))
        .orElseGet(() -> {
          log.info("No records found in the DB calling the NBAClient with game ID [{}]", id);
          Game emptyGame = Game.builder().build();

          NBAGame nbaGame = nbaClient.getGame(id).block();
          if (isEmpty(nbaGame)) {
            log.info("No game returned from the NBAClient for id [{}]", id);
            return emptyGame;
          }

          NBAGameStatPage gameStatPage = nbaClient.getGameStats(id, null).block();

          List<GameEntity> gameEntities = gameRepository
              .saveAll(toGameEntities(Arrays.asList(nbaGame)));
          playerRepository.saveAll(toPlayerEntities(gameStatPage.getGameStatList(), gameEntities));

          return gameEntities.stream()
              .findAny()
              .map(this::toGame)
              .orElse(emptyGame);
        });
  }

  @Transactional
  public List<Game> getGamesByDate(final Date date) {
    List<Game> games = gameRepository.findByDate(date)
        .stream()
        .map(this::toGame)
        .collect(Collectors.toList());

    if (isEmpty(games)) {
      log.info("No records found in the DB, calling the NBAClient for date [{}]", date);
      NBAGamePage nbaGamePage = nbaClient.getGamesByDate(date).block();

      if (isEmpty(nbaGamePage.getGames())) {
        log.info("No gameS returned from the NBAClient for date [{}]", date);
        return emptyList();
      }

      NBAGameStatPage gameStatPage = nbaClient.getGameStats(null, date).block();

      List<GameEntity> gameEntities = gameRepository
          .saveAll(toGameEntities(nbaGamePage.getGames()));
      playerRepository.saveAll(toPlayerEntities(gameStatPage.getGameStatList(), gameEntities));

      games = gameEntities.stream()
          .map(this::toGame)
          .collect(Collectors.toList());
    }
    return games;
  }

  @Transactional
  public void createComment(String gameId, AddComment comment) {
    log.info("Adding comment to game with ID [{}]", gameId);
    GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(
        () -> new EntityNotFoundException(
            format("Game with id: %s has not been found", gameId)));

    gameEntity.getCommentEntities().add(toCommentEntity(comment.getText(), gameEntity));
    log.debug("Successfully added comment to game with ID [{}]", gameId);
  }

  @Transactional
  public void updateComment(String gameId, Comment comment) {
    log.info("Updating comment ID [{}], for Game ID [{}]", comment.getId(), gameId);
    UUID commentUuid = UUID.fromString(comment.getId());

    CommentEntity commentEntity = commentRepository.findById(commentUuid).orElseThrow(
        () -> new EntityNotFoundException(
            format("Comment with id: %s has not been found", commentUuid)));

    commentEntity.setComment(comment.getText());
    log.debug("Successfully update comment for comment ID [{}]", commentUuid);
  }

  @Transactional
  public void deleteComment(String gameId, String commentId) {
    log.info("Deleting comment ID [{}], for Game ID [{}]", commentId, gameId);
    UUID commentUuid = UUID.fromString(commentId);
    CommentEntity commentEntity = commentRepository.findById(commentUuid).orElseThrow(
        () -> new EntityNotFoundException(
            format("Comment with id: %s has not been found", commentUuid)));

    gameRepository.findById(gameId).ifPresent(gameEntity -> {
      gameEntity.getCommentEntities().remove(commentEntity);
      gameRepository.save(gameEntity);
    });

    log.debug("Successfully deleted comment, ID [{}]", commentUuid);
  }

  private CommentEntity toCommentEntity(String comment, GameEntity gameEntity) {
    return CommentEntity.builder()
        .comment(comment)
        .game(gameEntity)
        .build();
  }

  protected List<GameEntity> toGameEntities(List<NBAGame> nbaGames) {
    return nbaGames.stream()
        .map(this::toGameEntity)
        .collect(Collectors.toList());
  }

  private GameEntity toGameEntity(NBAGame nbaGame) {
    return GameEntity.builder()
        .id(nbaGame.getId())
        .date(nbaGame.getDate())
        .homeScore(nbaGame.getHomeScore())
        .homeName(nbaGame.getHomeTeam().getName())
        .visitorName(nbaGame.getVisitorTeam().getName())
        .visitorScore(nbaGame.getVisitorScore())
        .build();
  }

  protected List<PlayerEntity> toPlayerEntities(List<NBAStat> stats,
      List<GameEntity> gameEntities) {

    //Create a map for easy referencing
    Map<String, GameEntity> gameEntityMap = gameEntities.stream()
        .collect(Collectors.toMap(GameEntity::getId, entity -> entity));

    return stats.stream()
        .filter(s -> s.getPoints() > 0)
        .map(s -> toPlayerEntity(s, gameEntityMap))
        .filter(s -> !isEmpty(s))
        .collect(Collectors.toList());
  }

  private PlayerEntity toPlayerEntity(NBAStat nbaGameStat,
      Map<String, GameEntity> gameEntityMap) {

    NBAStatPlayer player = nbaGameStat.getPlayer();
    GameEntity gameEntity = gameEntityMap.get(nbaGameStat.getGame().getId());

    if (isEmpty(gameEntity)) {
      log.info("We couldn't find a corresponding game, there are massive issues in "
          + "this NBAClientAPI, ignoring");
      return null;
    }

    PlayerEntity playerEntity = PlayerEntity.builder()
        .firstName(player.getFirstName())
        .lastName(player.getLastName())
        .game(gameEntity)
        .score(nbaGameStat.getPoints()).build();

    gameEntity.getPlayerEntities().add(playerEntity);
    return playerEntity;
  }

  private Game toGame(GameEntity entity) {
    return Game.builder()
        .id(entity.getId())
        .date(entity.getDate())
        .homeTeam(entity.getHomeName())
        .homeScore(entity.getHomeScore())
        .visitorTeam(entity.getVisitorName())
        .visitorScore(entity.getVisitorScore())
        .comments(toComments(entity.getCommentEntities()))
        .playersWithPoints(toPlayers(entity.getPlayerEntities()))
        .build();
  }

  private List<Comment> toComments(Set<CommentEntity> commentSet) {
    return commentSet.stream()
        .map(this::toComment)
        .collect(Collectors.toList());
  }

  private Comment toComment(CommentEntity entity) {
    return Comment.builder()
        .id(entity.getId().toString())
        .creationTime(entity.getCreationTime())
        .text(entity.getComment())
        .build();
  }

  private List<Player> toPlayers(Set<PlayerEntity> playerSet) {
    return playerSet.stream()
        .map(this::toPlayer)
        .collect(Collectors.toList());
  }

  private Player toPlayer(PlayerEntity entity) {
    return Player.builder().firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .score(entity.getScore())
        .build();
  }

}
