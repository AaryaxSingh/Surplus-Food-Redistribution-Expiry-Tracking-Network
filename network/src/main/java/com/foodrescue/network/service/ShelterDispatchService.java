package com.foodrescue.network.service;

import com.foodrescue.network.dto.DonationAlertDTO;
import com.foodrescue.network.model.FoodItem;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterDispatchService {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastDonationAlert(FoodItem item) {
        long hoursRemaining = Math.max(0, Duration.between(LocalDateTime.now(), item.getExpirationTime()).toHours());

        DonationAlertDTO alert = DonationAlertDTO.builder()
                .fooditemid(item.getId())
                .foodName(item.getName())
                .category(item.getCategory().name())
                .quantityKg(item.getQuantityKg())
                .vendorName(item.getVendor().getName())
                .vendorAddress(item.getVendor().getAddress())
                .vendorPhone(item.getVendor().getPhone())
                .expirationTime(item.getExpirationTime())
                .hoursRemaining(hoursRemaining)
                .alertMessage("Urgent: Free donation available!" + item.getQuantityKg() + "kg of" + item.getName()
                        + "ready for pickup")
                .build();

        log.info("Broadcasting WebSocket donation alert for item ID: {} ('{}')", item.getId(), item.getName());
        messagingTemplate.convertAndSend("/topic/shelter-alerts", alert);
    }
}
