package org.task.model.nbaconsumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class NBAStat {

  @JsonProperty("pts")
  int points;

  NBAStatPlayer player;

  NBAStatGame game;
}
