package com.complaints.api.controllers;

import com.complaints.api.dto.RegisterRequest;
import com.complaints.api.model.AuthRequest;
import com.complaints.api.model.User;
import com.complaints.api.providers.JwtTokenProvider;
import com.complaints.api.services.AuthService;
import com.complaints.api.services.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(com.complaints.api.config.TestSecurityConfig.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private JwtTokenProvider tokenProvider;

  @MockBean
  private AuthService authService;

  @MockBean
  private CustomUserDetailsService customUserDetailsService;

  @Test
  void shouldLoginSuccessfully() throws Exception {
    AuthRequest authRequest = new AuthRequest();
    authRequest.setCpf("123.456.789-00");
    authRequest.setPassword("password");

    Authentication authentication = Mockito.mock(Authentication.class);

    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(tokenProvider.generateToken(authentication)).thenReturn("fake-token");

    mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("fake-token"));
  }

  @Test
  void shouldRegisterUserSuccessfully() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setCpf("12345678900");
    request.setPassword("senha123");

    User user = new User();
    user.setId(1L);
    user.setCpf(request.getCpf());
    user.setName("João");

    when(authService.register(any())).thenReturn(user);

    mockMvc.perform(post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.cpf").value("12345678900"))
        .andExpect(jsonPath("$.name").value("João"));
  }
}
