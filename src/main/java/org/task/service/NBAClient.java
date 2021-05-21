package org.task.service;

import java.util.Date;
import org.task.model.nbaconsumer.NBAGame;
import org.task.model.nbaconsumer.NBAGamePage;
import org.task.model.nbaconsumer.NBAGameStatPage;
import reactor.core.publisher.Mono;

public interface NBAClient {
   Mono<NBAGame> getGame(final String gameId);

   Mono<NBAGamePage> getGamesByDate(final Date date);

   Mono<NBAGameStatPage> getGameStats(final String gameId, final Date date);
}
