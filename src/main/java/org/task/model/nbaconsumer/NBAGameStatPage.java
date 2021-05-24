package org.task.model.nbaconsumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBAGameStatPage {

  @JsonProperty("data")
  List<NBAStat> gameStatList;

}
