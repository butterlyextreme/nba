package org.task.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.task.model.producer.AddComment;
import org.task.model.producer.Comment;
import org.task.model.producer.Game;
import org.task.service.GameService;

import java.util.Date;
import java.util.List;

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

  @GetMapping(value = "/game")
  public ResponseEntity<List<Game>> getGamesByDate(
      @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
    List<Game> games = gameService.getGamesByDate(date);
    return new ResponseEntity<>(games, HttpStatus.OK);
  }

  @PostMapping(value = "/game/{id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createComment(
      @PathVariable final String id, @Valid @RequestBody AddComment comment) {
    gameService.createComment(id, comment);
    return new ResponseEntity<>(comment.getText(), HttpStatus.OK);
  }

  @PutMapping(value = "/game/{id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateComment(
      @PathVariable final String id, @Valid @RequestBody Comment comment) {
    gameService.updateComment(id, comment);
    return new ResponseEntity<>(comment.getText(), HttpStatus.OK);
  }

  @DeleteMapping(value = "/game/{id}/comment/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> updateComment(
      @PathVariable final String id, @PathVariable final String commentId) {
    gameService.deleteComment(id, commentId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
