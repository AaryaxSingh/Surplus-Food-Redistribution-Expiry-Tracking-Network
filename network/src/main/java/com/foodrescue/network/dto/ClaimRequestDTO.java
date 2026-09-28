package com.foodrescue.network.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequestDTO {

    @NotNull(message = "food item id is required")
    private Long foodItemId;

    @NotNull(message = "shelter id is required")
    private Long shelterId;

    private String notes;
}
