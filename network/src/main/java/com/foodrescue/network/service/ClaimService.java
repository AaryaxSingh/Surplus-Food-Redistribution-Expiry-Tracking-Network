package com.foodrescue.network.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.foodrescue.network.dto.ClaimRequestDTO;
import com.foodrescue.network.dto.ClaimResponseDTO;
import com.foodrescue.network.model.ClaimStatus;
import com.foodrescue.network.model.FoodItem;
import com.foodrescue.network.model.ListingStatus;
import com.foodrescue.network.model.PickupClaim;
import com.foodrescue.network.model.User;
import com.foodrescue.network.model.UserRole;
import com.foodrescue.network.repository.FoodItemRepository;
import com.foodrescue.network.repository.PickupClaimRepository;
import com.foodrescue.network.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClaimService {

    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;
    private final PickupClaimRepository pickupClaimRepository;

    @Transactional
    public ClaimResponseDTO claimDonation(ClaimRequestDTO req) {
        FoodItem item = foodItemRepository.findById(req.getFoodItemId())
                .orElseThrow(() -> new IllegalArgumentException("Food item not found with ID: " + req.getFoodItemId()));

        if (item.getStatus() == ListingStatus.CLAIMED) {
            throw new IllegalStateException("Food item is already claimed.");
        }
        if (item.getStatus() == ListingStatus.EXPIRED) {
            throw new IllegalStateException("Food item has already expired and cannot be claimed.");
        }

        User shelter = userRepository.findById(req.getShelterId())
                .orElseThrow(() -> new IllegalArgumentException("Shelter not found with ID: " + req.getShelterId()));
        if (shelter.getRole() != UserRole.ROLE_SHELTER) {
            throw new IllegalArgumentException("Only registered shelters can claim donations.");
        }

        item.setStatus(ListingStatus.CLAIMED);
        foodItemRepository.save(item);

        PickupClaim claim = PickupClaim.builder()
                .foodItem(item)
                .shelter(shelter)
                .claimTime(LocalDateTime.now())
                .pickupDeadline(item.getExpirationTime())
                .status(ClaimStatus.PENDING_PICKUP)
                .notes(req.getNotes())
                .build();

        PickupClaim saved = pickupClaimRepository.save(claim);
        log.info("New donation claim created. Item: {}, Shelter: {}", item.getId(), shelter.getId());
        return mapToDTO(saved);
    }

    @Transactional
    public ClaimResponseDTO completePickup(Long claimId) {

        PickupClaim claim = pickupClaimRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Claim not found: " + claimId));

        if (claim.getStatus() == ClaimStatus.COMPLETED) {
            throw new IllegalStateException("Claim is already marked as completed.");
        }
        claim.setStatus(ClaimStatus.COMPLETED);
        claim.setCompletedTime(LocalDateTime.now());
        PickupClaim saved = pickupClaimRepository.save(claim);

        log.info("Claim ID: {} marked COMPLETED. Salvaged {}kg!", claimId, saved.getFoodItem().getQuantityKg());
        return mapToDTO(saved);
    }

    private ClaimResponseDTO mapToDTO(PickupClaim claim) {
        return ClaimResponseDTO.builder()
                .claimId(claim.getId())
                .foodItemId(claim.getFoodItem().getId())
                .foodName(claim.getFoodItem().getName())
                .quantityKg(claim.getFoodItem().getQuantityKg())
                .shelterId(claim.getShelter().getId())
                .shelterName(claim.getShelter().getName())
                .claimTime(claim.getClaimTime())
                .pickupDeadline(claim.getPickupDeadline())
                .completedTime(claim.getCompletedTime())
                .status(claim.getStatus())
                .notes(claim.getNotes())
                .build();
    }

}
