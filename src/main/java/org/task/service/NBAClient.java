package org.task.service;

import java.util.List;
import org.task.model.nbaconsumer.NBAGame;
import org.task.model.nbaconsumer.NBAGameStatPage;
import reactor.core.publisher.Mono;

public interface NBAClient {
   Mono<NBAGame> getGame(final String gameId);

   Mono<NBAGameStatPage> getGameStats(final List<String> gameIds);
}
