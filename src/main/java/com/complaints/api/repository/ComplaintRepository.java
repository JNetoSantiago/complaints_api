package com.complaints.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.complaints.api.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
  Page<Complaint> findAllByCpf(String cpf, Pageable pageable);
}
