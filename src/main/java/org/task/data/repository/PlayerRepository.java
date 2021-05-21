package org.task.data.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.task.data.entity.PlayerEntity;

public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {

}
