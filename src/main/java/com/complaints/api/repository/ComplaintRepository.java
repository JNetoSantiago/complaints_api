package com.complaints.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.complaints.api.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
  List<Complaint> findAllByCpf(String cpf);
}
