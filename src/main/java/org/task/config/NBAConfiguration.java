package org.task.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.task.service.NBAClient;
import org.task.service.NBAClientImpl;

@Configuration
@RequiredArgsConstructor
public class NBAConfiguration {

  private final NBAConfigProperties nbaConfigProperties;

  @Bean
  public WebClient nbaWebClient(final WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl(nbaConfigProperties.getUrl()).build();
  }

  @Bean
  public NBAClient nbaClient(@Qualifier("nbaWebClient") WebClient webClient,
      NBAConfigProperties nbaConfigProperties) {
    return new NBAClientImpl(webClient, nbaConfigProperties);
  }
}
