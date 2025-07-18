package com.complaints.api.controllers;

import com.complaints.api.dto.ComplaintRequest;
import com.complaints.api.model.Complaint;
import com.complaints.api.providers.JwtTokenProvider;
import com.complaints.api.repository.ComplaintRepository;
import com.complaints.api.services.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ComplaintController.class)
public class ComplaintControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ComplaintRepository complaintRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private CustomUserDetailsService customUserDetailsService;

  @Test
  @WithMockUser(username = "12345678900")
  void shouldListUserComplaints() throws Exception {
    Complaint complaint = new Complaint();
    complaint.setId(1L);
    complaint.setCpf("12345678900");
    complaint.setTitle("Título");
    complaint.setDescription("Descrição");
    complaint.setCreatedAt(LocalDateTime.now());

    Page<Complaint> page = new PageImpl<>(List.of(complaint));

    when(complaintRepository.findAllByCpf(Mockito.eq("12345678900"), Mockito.any(Pageable.class)))
        .thenReturn(page);

    mockMvc.perform(get("/complaints?page=0&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].title").value("Título"));
  }

  @Test
  @WithMockUser(username = "12345678900")
  void shouldCreateComplaint() throws Exception {
    ComplaintRequest request = new ComplaintRequest();
    request.setTitle("Problema na entrega");
    request.setDescription("O pedido não chegou");

    mockMvc.perform(post("/complaints")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().string("Reclamação registrada com sucesso."));
  }

  @Test
  @WithMockUser(username = "12345678900")
  void shouldUpdateComplaintSuccessfully() throws Exception {
    Complaint existing = new Complaint();
    existing.setId(1L);
    existing.setCpf("12345678900");
    existing.setTitle("Antigo título");
    existing.setDescription("Antiga descrição");
    existing.setCreatedAt(LocalDateTime.now());

    ComplaintRequest updateRequest = new ComplaintRequest();
    updateRequest.setTitle("Novo título");
    updateRequest.setDescription("Nova descrição");

    when(complaintRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
    when(complaintRepository.save(Mockito.any(Complaint.class))).thenReturn(existing);

    mockMvc.perform(put("/complaints/1")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("Reclamação atualizada com sucesso."));
  }

  @Test
  @WithMockUser(username = "12345678900")
  void shouldNotUpdateComplaintOfAnotherUser() throws Exception {
    Complaint other = new Complaint();
    other.setId(2L);
    other.setCpf("00000000000");
    other.setTitle("Outro título");
    other.setDescription("Outra descrição");
    other.setCreatedAt(LocalDateTime.now());

    ComplaintRequest updateRequest = new ComplaintRequest();
    updateRequest.setTitle("Novo título");
    updateRequest.setDescription("Nova descrição");

    when(complaintRepository.findById(2L)).thenReturn(java.util.Optional.of(other));

    mockMvc.perform(put("/complaints/2")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Reclamação não encontrada ou não pertence ao usuário."));
  }

  @Test
  @WithMockUser(username = "12345678900")
  void shouldDeleteComplaintSuccessfully() throws Exception {
    Complaint existing = new Complaint();
    existing.setId(1L);
    existing.setCpf("12345678900");
    existing.setTitle("Título");
    existing.setDescription("Descrição");
    existing.setCreatedAt(LocalDateTime.now());

    when(complaintRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));

    mockMvc.perform(delete("/complaints/1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("Reclamação deletada com sucesso."));
  }

  @Test
  @WithMockUser(username = "12345678900")
  void shouldNotDeleteComplaintOfAnotherUser() throws Exception {
    Complaint other = new Complaint();
    other.setId(2L);
    other.setCpf("00000000000");
    other.setTitle("Outro título");
    other.setDescription("Outra descrição");
    other.setCreatedAt(LocalDateTime.now());

    when(complaintRepository.findById(2L)).thenReturn(java.util.Optional.of(other));

    mockMvc.perform(delete("/complaints/2").with(csrf()))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Reclamação não encontrada ou não pertence ao usuário."));
  }
}
