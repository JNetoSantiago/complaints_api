package com.complaints.api.controllers;

import java.util.List;

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

import com.complaints.api.dto.ComplaintRequest;
import com.complaints.api.dto.ComplaintSummaryResponse;
import com.complaints.api.model.Complaint;
import com.complaints.api.repository.ComplaintRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {
  @Autowired
  private ComplaintRepository complaintRepository;

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

    Complaint complaint = new Complaint();
    complaint.setCpf(cpf);
    complaint.setTitle(request.getTitle());
    complaint.setDescription(request.getDescription());

    complaintRepository.save(complaint);

    return ResponseEntity.ok("Reclamação registrada com sucesso.");
  }
}
