package org.task.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.task.model.producer.Game;
import org.task.service.GameService;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {


  private final GameService gameService;

  @GetMapping(value = "/game/{id}")
  public ResponseEntity<Game> getGameById(@PathVariable final String id) {
    Game game = gameService.getGameById(id);
    return new ResponseEntity<>(game, HttpStatus.OK);
  }

}
