package com.foodrescue.network.service;

import com.foodrescue.network.dto.ImpactSummaryDTO;
import com.foodrescue.network.model.ClaimStatus;
import com.foodrescue.network.model.FoodCategory;
import com.foodrescue.network.model.PickupClaim;
import com.foodrescue.network.repository.PickupClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImpactReportingService {

    private final PickupClaimRepository claimRepository;

    @Transactional(readOnly = true)
    public ImpactSummaryDTO generateImpactMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        // Find all claims that were actually completed
        List<PickupClaim> completedClaims = claimRepository.findCompletedClaimBetween(ClaimStatus.COMPLETED, startDate,
                endDate);

        double totalKg = 0.0;
        double totalCo2OffsetKg = 0.0;

        for (PickupClaim claim : completedClaims) {
            double kg = claim.getFoodItem().getQuantityKg();
            totalKg += kg;
            totalCo2OffsetKg += (kg * getEmissionFactor(claim.getFoodItem().getCategory()));
        }

        double metricTonsSalvaged = totalKg / 1000.0;
        long mealsProvided = Math.round(totalKg / 0.50); // standard: 0.5kg per meal

        log.info("Impact Report Generated: {} kg salvaged, {} kg CO2 offset, {} meals provided.",
                totalKg, totalCo2OffsetKg, mealsProvided);

        return ImpactSummaryDTO.builder()
                .totalKgSalvaged(totalKg)
                .metricTonsSalvaged(metricTonsSalvaged)
                .totalCo2OffsetKg(totalCo2OffsetKg)
                .mealsProvided(mealsProvided)
                .totalCompletedClaims(completedClaims.size())
                .build();
    }

    // Represents the kg of CO2 equivalent prevented per kg of food saved from
    // landfill.
    private double getEmissionFactor(FoodCategory category) {
        return switch (category) {
            case MEAT -> 6.0;
            case DAIRY -> 3.2;
            case PREPARED_MEALS -> 2.5;
            case BAKERY -> 1.8;
            case PRODUCE -> 1.2;
            case CANNED_PACKAGED -> 1.5;
        };
    }
}
