package com.foodrescue.network.repository;

import com.foodrescue.network.model.ClaimStatus;
import com.foodrescue.network.model.PickupClaim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PickupClaimRepository extends JpaRepository<PickupClaim, Long> {

    List<PickupClaim> findByShelterId(Long shelterId);

    Optional<PickupClaim> findByFoodItemId(Long foodItemId);

    // Used by our Impact Engine to compute total metric tons and carbon offset
    @Query("""
            SELECT c FROM PickupClaim c
            WHERE c.status = :status AND c.completedTime BETWEEN
            :startDate AND :endDate
            """)
    List<PickupClaim> findCompletedClaimBetween(
            @Param("status") ClaimStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
