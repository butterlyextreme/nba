package org.task.model.producer;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddComment {
  @NotNull
  String text;
}
