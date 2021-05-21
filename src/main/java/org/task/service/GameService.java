package org.task.service;


import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.task.model.producer.AddComment;
import org.task.model.producer.Comment;
import org.task.model.producer.Game;


public interface GameService {

   Game getGameById(final String id);
   List<Game> getGamesByDate(final Date date);
   void createComment(String gameId, AddComment comment);
   void deleteComment(String gameId, String commentId);
   void updateComment(String gameId, Comment comment);


}
