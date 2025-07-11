package com.complaints.api.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.complaints.api.model.User;
import com.complaints.api.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
    User user = userRepository.findByCpf(cpf)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

    return new org.springframework.security.core.userdetails.User(
        user.getCpf(), user.getPassword(), new ArrayList<>());
  }

}
