package com.complaints.api.dto;

import lombok.Data;

@Data
public class RegisterRequest {
  private String cpf;
  private String password;
}
