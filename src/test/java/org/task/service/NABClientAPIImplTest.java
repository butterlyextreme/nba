package org.task.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.task.config.NBAConfigProperties;
import org.task.config.NBAConfiguration;
import reactor.test.StepVerifier;

@EnableConfigurationProperties({NBAConfigProperties.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {NBAClient.class})
@ContextConfiguration(
    classes = {NBAConfiguration.class, JacksonAutoConfiguration.class,
        WebClientAutoConfiguration.class},
    initializers = ConfigFileApplicationContextInitializer.class)
@TestPropertySource(
    properties = {"spring.main.banner-mode=off",
        "logging.level.reactor.netty.http.client=DEBUG",
        "nba.client.url=https://free-nba.p.rapidapi.com",
        "nba.client.apiKeyHeader=480f1e37b5mshe1584c5617c89f4p144dacjsn86a1167e4247",
        "nba.client.hostHeader=free-nba.p.rapidapi.com"})
public class NABClientAPIImplTest {

  public static final String GAME_ID = "45237";

  @Autowired
  NBAClient nbaClientAPI;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer;

  private static WireMockServer wireMockServer;

  // @BeforeAll
  static void startWireMock() {
    wireMockServer = new WireMockServer(
        options().port(7005).withRootDirectory("src/test/resources/wiremock"));
    wireMockServer.start();
  }

  //@AfterAll
  static void stopWireMock() {
    wireMockServer.stop();
  }

  @Test
  public void executeGetGameById() {
    StepVerifier.create(nbaClientAPI.getGame(GAME_ID))
        .assertNext(response -> {
          assertEquals(GAME_ID, response.getId());
        })
        .verifyComplete();
  }

  @Test
  public void executeGetGameStatsByGameIds() {
    StepVerifier.create(nbaClientAPI.getGameStats(GAME_ID,null))
        .assertNext(response -> {
          assertEquals(25, response.getGameStatList().size());
          System.err.println((response.getGameStatList()));
        })
        .verifyComplete();
  }

  static String readMockJson(final String name) throws Exception {
    return new String(Files.readAllBytes(Paths.get(
        NABClientAPIImplTest.class.getResource("/wiremock/__files/" + name).toURI())));
  }

  /**
   * Deserializes json to the given class, using default ObjectMapper from jackson.
   */
  <T> T deserialize(final String content, final Class<T> valueType) {
    try {
      return objectMapper.readValue(content, valueType);
    } catch (final IOException exception) {
      throw new RuntimeException(exception);
    }
  }
}
