package org.task.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.task.data.entity.GameEntity;

public interface GameRepository extends JpaRepository<GameEntity,String> {

}
