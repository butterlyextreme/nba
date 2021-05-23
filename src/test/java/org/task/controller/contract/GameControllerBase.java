package org.task.controller.contract;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.task.controller.ResourceNotFoundException;
import org.task.service.GameService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GameControllerBase {

  @LocalServerPort
  private int port;

  @MockBean
  private GameService gameService;

  private static String NOT_FOUND = "43569ed-d170-41ee-81a0-8a257c1646ee";
  private static String INTERNAL_ERROR = "43569ed-d170-41ee-81a0-8a257c1646ff";
  private static String GAME_ID = "43257";

  @BeforeEach
  public void clear() {
    reset(gameService);
    doThrow(new ResourceNotFoundException("not found")).when(gameService)
        .deleteComment(eq(GAME_ID), eq(NOT_FOUND));
    doThrow(new RuntimeException("internal_error")).when(gameService)
        .deleteComment(eq(GAME_ID), eq(INTERNAL_ERROR));
  }


  @BeforeAll
  public void setUp() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

}
