package org.task.service;


import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.task.data.entity.GameEntity;
import org.task.data.entity.PlayerEntity;
import org.task.data.repository.GameRepository;
import org.task.data.repository.PlayerRepository;
import org.task.model.nbaconsumer.NBAGame;
import org.task.model.nbaconsumer.NBAStat;
import org.task.model.nbaconsumer.NBAGameStatPage;
import org.task.model.nbaconsumer.NBAStatPlayer;
import org.task.model.producer.Game;

@Slf4j
@AllArgsConstructor
@Service
public class GameServiceImpl implements GameService {

  private final GameRepository gameRepository;
  private final PlayerRepository playerRepository;
  private final NBAClient nbaClient;

  @Transactional
  public Game getGameById(final String id) {
    return gameRepository.findById(id).map(gameEntity -> {
      return this.toGame(gameEntity);
    }).orElseGet(() -> {
      log.info("No records found in the DB");
      NBAGame nbaGame = nbaClient.getGame(id).block();
      if (isEmpty(nbaGame)) {
        log.info("No game returned from the nbaApiClient");
        return null;
      }

      NBAGameStatPage gameStatPage = nbaClient.getGameStats(Arrays.asList(id)).block();

      List<GameEntity> gameEntities = gameRepository
          .saveAll(toGameEntities(Arrays.asList(nbaGame)));
      playerRepository.saveAll(toPlayerEntities(gameStatPage.getGameStatList(), gameEntities));

      return gameEntities.stream()
          .map(this::toGame)
          .findFirst()
          .get();
    });
  }

  protected List<GameEntity> toGameEntities(List<NBAGame> nbaGames) {
    return nbaGames.stream()
        .map(this::toGameEntity)
        .collect(Collectors.toList());
  }

  private GameEntity toGameEntity(NBAGame nbaGame) {
    return GameEntity.builder()
        .id(nbaGame.getId())
        .homeScore(nbaGame.getHomeScore())
        .visitorScore(nbaGame.getVisitorScore()).build();
  }

  protected List<PlayerEntity> toPlayerEntities(List<NBAStat> stats,
      List<GameEntity> gameEntities) {

    Map<String, GameEntity> gameEntityMap = gameEntities.stream()
        .collect(Collectors.toMap(GameEntity::getId, entity -> entity));

    return stats.stream().filter(s -> s.getPoints() > 0)
        .map(s -> toPlayerEntity(s, gameEntityMap))
        .collect(Collectors.toList());
  }

  private PlayerEntity toPlayerEntity(NBAStat nbaGameStat,
      Map<String, GameEntity> gameEntityMap) {
    NBAStatPlayer player = nbaGameStat.getPlayer();
    return PlayerEntity.builder().firstName(player.getFirstName())
        .lastName(player.getLastName())
        .game(gameEntityMap.get(nbaGameStat.getGame().getId()))
        .score(nbaGameStat.getPoints()).build();
  }

  private Game toGame(GameEntity entity) {
    return Game.builder()
        .id(entity.getId())
        .homeTeam(entity.getHomeName())
        .homeScore(entity.getHomeScore()).build();
  }

}
