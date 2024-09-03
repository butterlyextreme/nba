package org.task.data.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Table(name = "game")
@Entity(name = "game")
public class GameEntity {

  @Id
  private String id;
  @Temporal(TemporalType.DATE)
  @Column(name = "date")
  private Date date;
  private String homeName;
  private String visitorName;
  private int homeScore;
  private int visitorScore;

  @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
  @Fetch(value = FetchMode.JOIN)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OrderBy("creation_time DESC")
  private Set<CommentEntity> commentEntities = new HashSet<>();

  @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
  @Fetch(value = FetchMode.JOIN)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  private Set<PlayerEntity> playerEntities = new HashSet<>();

}
