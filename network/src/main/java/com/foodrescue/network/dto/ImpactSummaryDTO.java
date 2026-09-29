package com.foodrescue.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImpactSummaryDTO {
    private Double totalKgSalvaged;
    private Double metricTonsSalvaged;
    private Double totalCo2OffsetKg;
    private Long mealsProvided;
    private Integer totalCompletedClaims;
}
