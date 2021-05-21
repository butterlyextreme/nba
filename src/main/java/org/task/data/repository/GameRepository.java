package org.task.data.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.task.data.entity.GameEntity;

public interface GameRepository extends JpaRepository<GameEntity,String> {

  List<GameEntity> findByDate(Date date);
}
