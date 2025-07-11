package com.complaints.api.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.complaints.api.dto.RegisterRequest;
import com.complaints.api.model.AuthRequest;
import com.complaints.api.model.User;
import com.complaints.api.providers.JwtTokenProvider;
import com.complaints.api.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getCpf(), request.getPassword()));

    String token = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(Collections.singletonMap("token", token));
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
    User user = authService.register(request);
    return ResponseEntity.ok(user);
  }
}