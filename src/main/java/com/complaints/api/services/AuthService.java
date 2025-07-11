package com.complaints.api.services;

import com.complaints.api.dto.RegisterRequest;
import com.complaints.api.model.User;
import com.complaints.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User register(RegisterRequest request) {
    String cleanedCpf = request.getCpf().replaceAll("[^\\d]", "");

    if (userRepository.findByCpf(cleanedCpf).isPresent()) {
      throw new RuntimeException("CPF já cadastrado.");
    }

    User user = User.builder()
        .cpf(cleanedCpf)
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    return userRepository.save(user);
  }
}
