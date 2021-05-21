package org.task.model.producer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;
import org.task.model.nbaconsumer.NBATeam;

@Data
@Builder
public class Game {
  String id;
  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss z")
  ZonedDateTime date;
  @JsonProperty("home_team")
  String homeTeam;
  @JsonProperty("home_team_score")
  int homeScore;
  @JsonProperty("visitor_team")
  String visitorTeam;
  @JsonProperty("visitor_team_score")
  int visitorScore;

}
