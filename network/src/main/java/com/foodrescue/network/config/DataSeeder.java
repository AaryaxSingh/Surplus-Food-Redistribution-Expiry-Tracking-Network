package com.foodrescue.network.config;

import com.foodrescue.network.model.*;
import com.foodrescue.network.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;
    private final PickupClaimRepository claimRepository;
    private final PasswordEncoder passwordEncoder; // Added for security

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0)
            return;

        // 1. Create a Master Admin
        userRepository.save(User.builder()
                .name("System Admin")
                .email("admin@foodrescue.net")
                .password(passwordEncoder.encode("admin123"))
                .role(UserRole.ROLE_ADMIN)
                .build());

        // 2. Create a Vendor
        User bakery = userRepository.save(User.builder()
                .name("Artisan Crust Bakery")
                .email("contact@artisancrust.com")
                .password(passwordEncoder.encode("vendor123"))
                .role(UserRole.ROLE_VENDOR)
                .address("124 Market Street, Downtown")
                .phone("+1-555-0199")
                .latitude(37.7749)
                .longitude(-122.4194)
                .build());

        // 3. Create a Shelter Coordinator
        User shelter = userRepository.save(User.builder()
                .name("Hope Community Kitchen")
                .email("coordinator@hopekitchen.org")
                .password(passwordEncoder.encode("shelter123"))
                .role(UserRole.ROLE_SHELTER)
                .address("500 4th Street, Downtown")
                .phone("+1-555-0244")
                .latitude(37.7810)
                .longitude(-122.4010)
                .build());

        // 4. Normal sale item (>24 hours shelf life)
        foodItemRepository.save(FoodItem.builder()
                .name("Baguettes & Brioche Loaves")
                .category(FoodCategory.BAKERY)
                .quantityKg(15.0)
                .originalPrice(BigDecimal.valueOf(45.00))
                .currentPrice(BigDecimal.valueOf(45.00))
                .expirationTime(LocalDateTime.now().plusHours(30))
                .status(ListingStatus.ACTIVE_SALE)
                .vendor(bakery)
                .build());

        // 5. Critical donation item (expires in 3 hours - triggers WebSocket alert)
        foodItemRepository.save(FoodItem.builder()
                .name("Prepared Chicken & Rice Bowls")
                .category(FoodCategory.PREPARED_MEALS)
                .quantityKg(25.0)
                .originalPrice(BigDecimal.valueOf(150.00))
                .currentPrice(BigDecimal.ZERO)
                .expirationTime(LocalDateTime.now().plusHours(3))
                .status(ListingStatus.DONATION_CRITICAL)
                .vendor(bakery)
                .build());

        // 6. Already completed pickup claim
        FoodItem completedItem = foodItemRepository.save(FoodItem.builder()
                .name("Surplus Whole Grain Bread")
                .category(FoodCategory.BAKERY)
                .quantityKg(10.0)
                .originalPrice(BigDecimal.valueOf(30.00))
                .currentPrice(BigDecimal.ZERO)
                .expirationTime(LocalDateTime.now().minusHours(2))
                .status(ListingStatus.CLAIMED)
                .vendor(bakery)
                .build());

        claimRepository.save(PickupClaim.builder()
                .foodItem(completedItem)
                .shelter(shelter)
                .status(ClaimStatus.COMPLETED)
                .claimTime(LocalDateTime.now().minusHours(3))
                .completedTime(LocalDateTime.now().minusHours(1))
                .notes("Picked up on time by volunteer driver.")
                .build());

        System.out.println("=========================================");
        System.out.println(">>> Sample seed data + MASTER ADMIN created!");
        System.out.println(">>> Admin Email: admin@foodrescue.net");
        System.out.println(">>> Admin Pass: admin123");
        System.out.println("=========================================");
    }
}