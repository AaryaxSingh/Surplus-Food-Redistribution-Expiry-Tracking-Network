package com.foodrescue.network.dto;

import java.time.LocalDateTime;

import com.foodrescue.network.model.ClaimStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimResponseDTO {

    private Long claimId;
    private Long foodItemId;
    private String foodName;
    private Double quantityKg;
    private Long shelterId;
    private String shelterName;
    private LocalDateTime claimTime;
    private LocalDateTime pickupDeadline;
    private LocalDateTime completedTime;
    private ClaimStatus status;
    private String notes;
}
