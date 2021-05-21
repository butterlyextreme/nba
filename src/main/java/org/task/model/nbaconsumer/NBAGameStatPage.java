package org.task.model.nbaconsumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class NBAGameStatPage {

  @JsonProperty("data")
  List<NBAStat> gameStatList;

}
