package com.complaints.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.complaints.api.dto.ComplaintRequest;
import com.complaints.api.dto.ComplaintSummaryResponse;
import com.complaints.api.model.Complaint;
import com.complaints.api.repository.ComplaintRepository;
import com.complaints.api.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {
  @Autowired
  private ComplaintRepository complaintRepository;
  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public ResponseEntity<?> listUserComplaints(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      Authentication authentication) {
    String cpf = authentication.getName();

    Pageable pageable = PageRequest.of(page, size);
    Page<Complaint> complaints = complaintRepository.findAllByCpf(cpf, pageable);

    Page<ComplaintSummaryResponse> responsePage = complaints.map(c -> new ComplaintSummaryResponse(
        c.getId(),
        c.getTitle(),
        c.getDescription(),
        c.getCreatedAt()));

    return ResponseEntity.ok(responsePage);
  }

  @PostMapping
  public ResponseEntity<?> createComplaint(@Valid @RequestBody ComplaintRequest request,
      Authentication authentication) {

    String cpf = authentication.getName();
    // Buscar usuário pelo CPF
    var userOpt = userRepository.findByCpf(cpf);
    if (userOpt.isEmpty()) {
      return ResponseEntity.status(404).body("Usuário não encontrado.");
    }
    var user = userOpt.get();

    Complaint complaint = new Complaint();
    complaint.setCpf(cpf);
    complaint.setUserId(user.getId());
    complaint.setTitle(request.getTitle());
    complaint.setDescription(request.getDescription());

    complaintRepository.save(complaint);

    return ResponseEntity.ok("Reclamação registrada com sucesso.");
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateComplaint(@PathVariable Long id, @Valid @RequestBody ComplaintRequest request,
      Authentication authentication) {
    String cpf = authentication.getName();
    return complaintRepository.findById(id)
        .filter(c -> c.getCpf().equals(cpf))
        .map(complaint -> {
          complaint.setTitle(request.getTitle());
          complaint.setDescription(request.getDescription());
          complaintRepository.save(complaint);
          return ResponseEntity.ok("Reclamação atualizada com sucesso.");
        })
        .orElse(ResponseEntity.status(404).body("Reclamação não encontrada ou não pertence ao usuário."));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteComplaint(@PathVariable Long id, Authentication authentication) {
    String cpf = authentication.getName();
    return complaintRepository.findById(id)
        .filter(c -> c.getCpf().equals(cpf))
        .map(complaint -> {
          complaintRepository.delete(complaint);
          return ResponseEntity.ok("Reclamação deletada com sucesso.");
        })
        .orElse(ResponseEntity.status(404).body("Reclamação não encontrada ou não pertence ao usuário."));
  }
}
