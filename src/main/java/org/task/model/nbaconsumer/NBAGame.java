package org.task.model.nbaconsumer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NBAGame {
  String id;
  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss z")
  ZonedDateTime date;
  @JsonProperty("home_team")
  NBATeam homeTeam;
  @JsonProperty("home_team_score")
  int homeScore;
  @JsonProperty("visitor_team")
  NBATeam visitorTeam;
  @JsonProperty("visitor_team_score")
  int visitorScore;
}
