package com.foodrescue.network.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.foodrescue.network.model.FoodItem;
import com.foodrescue.network.model.ListingStatus;
import com.foodrescue.network.repository.FoodItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpiryDecayScheduler {

    private final FoodItemRepository foodItemRepository;
    private final ShelterDispatchService dispatchService;

    // run every 60 seconds
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void processExpiryDecayAndAlert() {
        LocalDateTime now = LocalDateTime.now();
        List<FoodItem> activeItems = foodItemRepository.findAllActiveForDecayEvaluation();

        if (activeItems.isEmpty()) {
            return;
        }

        log.info("Running Expiry Decay Scheduler on {} Active items...", activeItems.size());

        for (FoodItem item : activeItems) {
            Duration remaining = Duration.between(now, item.getExpirationTime());
            long hoursLeft = remaining.toHours();

            if (remaining.isNegative()) {
                // item expired
                if (item.getStatus() != ListingStatus.EXPIRED) {
                    item.setStatus(ListingStatus.EXPIRED);
                    log.info("Item '{}' (ID: {}) has EXPIRED.\", item.getName(), item.getId()");
                }
            } else if (hoursLeft <= 6) {
                // Critical donation window
                if (item.getStatus() != ListingStatus.DONATION_CRITICAL) {
                    item.setStatus(ListingStatus.DONATION_CRITICAL);
                    item.setCurrentPrice(BigDecimal.ZERO);
                    log.info("Item '{}' (ID: {}) reached DONATION_CRITICAL! Triggering dispatch...", item.getName(),
                            item.getId());
                    dispatchService.broadcastDonationAlert(item);
                }
            } else if (hoursLeft <= 12) {
                // TIER 2: 60% discount
                if (item.getStatus() != ListingStatus.DISCOUNTED_TIER_2) {
                    item.setStatus(ListingStatus.DISCOUNTED_TIER_2);

                    item.setCurrentPrice(item.getOriginalPrice().multiply(BigDecimal.valueOf(0.40)));
                    log.info("Item '{}' (ID: {}) shifted to DISCOUNTED_TIER_2 (60% off).", item.getName(),
                            item.getId());
                }
            } else if (hoursLeft <= 24) {
                // Tier 1: 30% discount
                if (item.getStatus() != ListingStatus.DISCOUNTED_TIER_1) {
                    item.setStatus(ListingStatus.DISCOUNTED_TIER_1);
                    item.setCurrentPrice(item.getOriginalPrice().multiply(BigDecimal.valueOf(0.70)));
                    log.info("Item '{}' (ID: {}) shifted to DISCOUNTED_TIER_1 (30% off).", item.getName(),
                            item.getId());
                }
            }
        }
        foodItemRepository.saveAll(activeItems);
    }

}
