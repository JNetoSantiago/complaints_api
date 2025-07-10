package com.complaints.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String cpf;

  @Column(nullable = false)
  private String password;

  public Long getId() {
    return id;
  }

  public String getCpf() {
    return cpf;
  }

  public String getPassword() {
    return password;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
