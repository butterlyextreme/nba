package org.task.data.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.task.data.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {

}
