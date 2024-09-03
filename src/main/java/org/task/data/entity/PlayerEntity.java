package org.task.data.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Table(name = "player")
@Entity(name = "player")
public class PlayerEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID id;

  private String firstName;
  private String lastName;
  private int score;

  @ManyToOne
  @JoinColumn(name= "game_id", referencedColumnName = "id", nullable = false)
  private GameEntity game;

}
