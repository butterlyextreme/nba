package org.task.service.data.repository;

import java.time.Instant;
import org.task.data.entity.CommentEntity;
import org.task.data.entity.GameEntity;

public class GameHelper {

  public static String GAME_ID = "4124";

  public static GameEntity createGameEntity() {
    return GameEntity.builder().homeName("Hornets")
        .homeScore(2)
        .id(GAME_ID)
        .visitorName("Celtics")
        .visitorScore(3)
        //.date(Instant.now())
        .build();
  }

  public static CommentEntity createCommentEntity(String comment, GameEntity gameEntity) {
    return CommentEntity.builder()
        .game(gameEntity)
        .comment(comment).build();
  }

}
