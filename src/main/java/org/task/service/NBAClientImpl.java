package org.task.service;

import static reactor.core.publisher.Mono.error;


import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.task.config.NBAConfigProperties;
import org.task.model.nbaconsumer.NBAGame;
import org.task.model.nbaconsumer.NBAGamePage;
import org.task.model.nbaconsumer.NBAGameStatPage;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class NBAClientImpl implements NBAClient {

  @Qualifier("nbaWebClient")
  private final WebClient nbaWebClient;
  private final NBAConfigProperties nbaConfigProperties;

  public Mono<NBAGame> getGame(final String gameId) {
    log.info("Retrieve game details for game Id [{}]", gameId);
    return nbaWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/games/{id}")
            .build(gameId))
        .headers(getAuthorisationHeaders())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .flatMap(response -> handleException(response,
            "Failed to get Game, HTTPStatus {}"))
        .flatMap(response -> response.bodyToMono(NBAGame.class));
  }

  public Mono<NBAGamePage> getGamesByDate(final Date date) {
    log.info("Retrieve game details for games on date [{}]", date);
    return nbaWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/games/")
            .queryParam("date", "{date}")
            .queryParam("per_page", "{per_page}")
            .queryParam("page", "{page}")
            .build(date, "25", "0"))
        .headers(getAuthorisationHeaders())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .flatMap(response -> handleException(response,
            "Failed to get Game, HTTPStatus {}"))
        .flatMap(response -> response.bodyToMono(NBAGamePage.class));
  }

  public Mono<NBAGameStatPage> getGameStats(final String gameId, final Date date) {
    log.info("Retrieve stat details for the following game Ids [{}] and date [{}]", gameId, date);
    return nbaWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/stats/")
            //.queryParam("game_ids", "{gameIds}")
            .queryParam("date", "{date}")
            .queryParam("per_page", "{per_page}")
            .queryParam("page", "{page}")
            .build(date, "25", "0"))
        .headers(getAuthorisationHeaders())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .flatMap(response -> handleException(response,
            "Failed to get Game, HTTPStatus {}"))
        .flatMap(response -> response.bodyToMono(NBAGameStatPage.class));
  }

  private Consumer<HttpHeaders> getAuthorisationHeaders() {
    return httpHeaders -> {
      httpHeaders.add("x-rapidapi-key", nbaConfigProperties.getApiKeyHeader());
      httpHeaders.add("x-rapidapi-host", nbaConfigProperties.getHostHeader());
      httpHeaders.add("useQueryString", "true");
    };
  }

  private static Mono<ClientResponse> handleException(final ClientResponse response,
      final String errorMessage) {
    final HttpStatus status = response.statusCode();
    if (!status.is2xxSuccessful()) {
      log.debug(errorMessage,
          response.statusCode());
      return response.bodyToMono(String.class)
          .flatMap(
              e -> error(
                  new Exception("Unable ")));
    } else {
      return Mono.just(response);
    }
  }

}
