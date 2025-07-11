package com.complaints.api.model;

import lombok.Data;

@Data
public class AuthRequest {
  private String cpf;
  private String password;

  public String getCpf() {
    return cpf;
  }

  public String getPassword() {
    return password;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
