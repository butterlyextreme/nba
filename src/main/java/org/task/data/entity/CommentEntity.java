package org.task.data.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Table(name = "comment")
@Entity(name = "comment")
public class CommentEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID id;
  private String comment;

  @CreationTimestamp
  @Column(name = "creation_time", updatable = false)
  private Date creationTime;

  @UpdateTimestamp
  @Column(name = "modified_time")
  private Date modifiedTime;

  @ManyToOne
  @JoinColumn(name= "game_id", referencedColumnName = "id", nullable = false)
  private GameEntity game;

}
