package org.task.model.producer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game {

  String id;
  Date date;
  @JsonProperty("home_team")
  String homeTeam;
  @JsonProperty("home_team_score")
  int homeScore;
  @JsonProperty("visitor_team")
  String visitorTeam;
  @JsonProperty("visitor_team_score")
  int visitorScore;
  @JsonProperty("players_with_points")
  List<Player> playersWithPoints;
  List<Comment> comments;
}
