package com.complaints.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.complaints.api.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByCpf(String cpf);
}
