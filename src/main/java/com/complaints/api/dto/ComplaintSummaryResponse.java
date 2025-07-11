package com.complaints.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComplaintSummaryResponse {
  private Long id;
  private String title;
  private String description;
  private LocalDateTime createdAt;
}
