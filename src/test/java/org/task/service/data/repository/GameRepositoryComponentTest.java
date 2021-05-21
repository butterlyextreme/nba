package org.task.service.data.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.task.service.data.repository.GameHelper.GAME_ID;
import static org.task.service.data.repository.GameHelper.createCommentEntity;
import static org.task.service.data.repository.GameHelper.createGameEntity;

import java.util.ArrayList;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.task.data.entity.CommentEntity;
import org.task.data.entity.GameEntity;
import org.task.data.repository.CommentRepository;
import org.task.data.repository.GameRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class GameRepositoryComponentTest {

  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private GameRepository gameRepository;

  @Test
  public void testAddBooking() {
    GameEntity gameEntity = createGameEntity();
    testEntityManager.persist(gameEntity);
    assertEquals(1, gameRepository.findAll().size());
  }

  @Test
  public void testAddCommentBooking() {
    createAndPersistGameEntity();
    GameEntity gameEntity = gameRepository.findById(GAME_ID).orElse(null);
    CommentEntity commentEntity = createCommentEntity("comment", gameEntity);

    gameEntity.getCommentEntities().add(commentEntity);
    testEntityManager.persistAndFlush(gameEntity);
    gameEntity = gameRepository.findById(GAME_ID).orElse(null);
    assertEquals(1, gameEntity.getCommentEntities().size());
  }

  @Test
    public void testRetrieveCommentsInOrder() {
      createAndPersistGameEntity();
      GameEntity gameEntity = gameRepository.findById(GAME_ID).orElse(null);
      createCommentEntity("commentOne", gameEntity);

      gameEntity.getCommentEntities().add(createCommentEntity("commentOne", gameEntity));
      testEntityManager.persistAndFlush(gameEntity);

      gameEntity.getCommentEntities().add(createCommentEntity("commentTwo", gameEntity));
      testEntityManager.persistAndFlush(gameEntity);

      gameEntity = gameRepository.findById(GAME_ID).orElse(null);
      assertEquals("commentTwo", gameEntity.getCommentEntities());
  }


  private void createAndPersistGameEntity() {
    GameEntity gameEntity = createGameEntity();
    testEntityManager.persist(gameEntity);
    assertEquals(1, gameRepository.findAll().size());
  }







}
