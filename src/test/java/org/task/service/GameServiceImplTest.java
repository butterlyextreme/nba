package org.task.service;


import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.task.data.entity.GameEntity;
import org.task.data.repository.GameRepository;
import org.task.data.repository.PlayerRepository;
import org.task.model.nbaconsumer.NBAGame;
import org.task.model.nbaconsumer.NBAGameStatPage;
import org.task.model.nbaconsumer.NBAStat;
import org.task.model.nbaconsumer.NBAStatGame;
import org.task.model.nbaconsumer.NBAStatPlayer;
import org.task.model.nbaconsumer.NBATeam;
import org.task.model.producer.Game;
import org.task.model.producer.Player;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

  @Mock
  private NBAClient nbaClient;

  @Mock
  private GameRepository gameRepository;

  @Mock
  private PlayerRepository playerRepository;

  @InjectMocks
  private GameServiceImpl gameService;

  private final Date now = new Date();

  private static final String GAME_ID = "123456";

  @Test
  public void testGetGameByIdNotCachedInDBOnePlayerScored() {
    NBAGame nbaGame = toNBAGame();
    NBAGameStatPage nbaGameStatPage = toNBAGameStatPage();

    when(gameRepository.findById(eq(GAME_ID))).thenReturn(Optional.empty());
    when(nbaClient.getGame(eq(GAME_ID))).thenReturn(Mono.just(nbaGame));
    when(nbaClient.getGameStatsByGameId(eq(GAME_ID))).thenReturn(Mono.just(nbaGameStatPage));

    List<GameEntity> gameEntities = gameService.toGameEntities(singletonList(nbaGame));
    gameService.toPlayerEntities(nbaGameStatPage.getGameStatList(), gameEntities);

    when(gameRepository.saveAll(any())).thenReturn(gameEntities);
    Game game = gameService.getGameById(GAME_ID);
    assertEquals(toGame(), game);
  }

  @Test
  public void testGetGameByIdCachedInDBOnePlayerScored() {
    NBAGame nbaGame = toNBAGame();
    NBAGameStatPage nbaGameStatPage = toNBAGameStatPage();
    List<GameEntity> gameEntities = gameService.toGameEntities(singletonList(nbaGame));
    gameService.toPlayerEntities(nbaGameStatPage.getGameStatList(), gameEntities);

    when(gameRepository.findById(eq(GAME_ID))).thenReturn(Optional.of(gameEntities.get(0)));

    Game game = gameService.getGameById(GAME_ID);

    verify(nbaClient, times(0)).getGame(eq(GAME_ID));
    verify(nbaClient, times(0)).getGameStatsByGameId(eq(GAME_ID));
    verify(gameRepository, times(0)).saveAll(any());
    verify(playerRepository, times(0)).saveAll(any());

    assertEquals(toGame(), game);
  }


  private Game toGame() {
    return Game.builder()
        .id(GAME_ID)
        .date(now)
        .homeTeam("hometeam")
        .homeScore(10)
        .visitorTeam("visitorteam")
        .visitorScore(20)
        .comments(Collections.emptyList())
        .playersWithPoints(singletonList(toPlayer()))
        .build();
  }

  private Player toPlayer() {
    return Player.builder().firstName("Harry")
        .lastName("Styles")
        .score(1)
        .build();
  }

  private NBAGame toNBAGame() {
    return NBAGame.builder()
        .id(GameServiceImplTest.GAME_ID)
        .date(now)
        .homeScore(10)
        .visitorScore(20)
        .visitorTeam(NBATeam.builder().id("12").name("visitorteam").build())
        .homeTeam(NBATeam.builder().id("89").name("hometeam").build())
        .build();
  }

  private NBAGameStatPage toNBAGameStatPage() {
    return NBAGameStatPage.builder()
        .gameStatList(Arrays.asList(toNBAStat("Harry", "Styles", 1),
            toNBAStat("Jeremy", "Lamb", 0)))
        .build();
  }

  private NBAStat toNBAStat(String firstName, String lastName, int points) {
    return NBAStat.builder().game(NBAStatGame.builder().id(GameServiceImplTest.GAME_ID).build())
        .points(1)
        .player(NBAStatPlayer.builder().firstName("Harry")
            .lastName("Styles")
            .build()).build();
  }
}
