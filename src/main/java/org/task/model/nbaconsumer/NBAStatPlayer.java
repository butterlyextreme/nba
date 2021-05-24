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
public class NBAStatPlayer {
  @JsonProperty("first_name")
  String firstName;

  @JsonProperty("last_name")
  String lastName;

}
