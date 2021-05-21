package org.task.config;

import static com.fasterxml.jackson.databind.AnnotationIntrospector.pair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import org.task.jackson.NBADateIntrospector;
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

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> builder
        .annotationIntrospector(
            pair(new JacksonAnnotationIntrospector(), new NBADateIntrospector()));
  }

}
