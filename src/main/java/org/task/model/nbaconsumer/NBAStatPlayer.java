package org.task.model.nbaconsumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NBAStatPlayer {
  @JsonProperty("first_name")
  String firstName;

  @JsonProperty("last_name")
  String lastName;

}
