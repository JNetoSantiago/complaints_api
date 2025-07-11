package com.complaints.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.complaints.api.dto.ComplaintRequest;
import com.complaints.api.model.Complaint;
import com.complaints.api.repository.ComplaintRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("complaints")
public class ComplaintController {
  @Autowired
  private ComplaintRepository complaintRepository;

  @PostMapping
  public ResponseEntity<?> createComplaint(@Valid @RequestBody ComplaintRequest request,
      Authentication authentication) {

    String cpf = authentication.getName();

    Complaint complaint = new Complaint();
    complaint.setCpf(cpf);
    complaint.setTitle(request.getTitle());
    complaint.setDescription(request.getDescription());

    complaintRepository.save(complaint);

    return ResponseEntity.ok("Reclamação registrada com sucesso.");
  }
}
