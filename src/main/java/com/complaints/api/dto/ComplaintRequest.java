package com.complaints.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintRequest {
  @NotBlank(message = "Título é obrigatório")
  private String title;

  @NotBlank(message = "Descrição é obrigatória")
  private String description;
}
