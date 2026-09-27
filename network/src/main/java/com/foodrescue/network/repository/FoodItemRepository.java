package com.foodrescue.network.repository;

import com.foodrescue.network.model.FoodItem;
import com.foodrescue.network.model.ListingStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByStatus(ListingStatus status);

    List<FoodItem> findByVendorId(Long vendorId);

    // Used by our background scheduler: find all items that can still decay or be
    // donated
    @Query("""
            SELECT f FROM FoodItem f
            WHERE f.status IN ('ACTIVE_SALE', 'DISCOUNTED_TIER1', 'DISCOUNTED_TIER2', 'DONATION_CRITICAL')
            """)
    List<FoodItem> findAllActiveForDecayEvaluation();
}
