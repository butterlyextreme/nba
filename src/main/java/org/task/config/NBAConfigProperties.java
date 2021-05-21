package org.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(value = "nba.client", ignoreUnknownFields = false)
public class NBAConfigProperties {

  /**
   * Maps to the configuration ${nba.client.url}
   */
  private String url;

  /**
   * Maps to the configuration ${nba.client.apikey}
   */
  private String apiKeyHeader;

  /**
   * Maps to the configuration ${nba.client.host}
   */
  private String hostHeader;


}
