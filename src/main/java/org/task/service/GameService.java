package org.task.service;


import org.springframework.stereotype.Service;
import org.task.model.producer.Game;


public interface GameService {

   Game getGameById(final String id);

}
