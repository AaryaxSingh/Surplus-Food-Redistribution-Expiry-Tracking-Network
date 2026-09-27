package com.foodrescue.network.config;

import com.foodrescue.network.model.*;
import com.foodrescue.network.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("dev")
@RequiredArgsConstructor // Lombok creates constructor injection automatically
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0)
            return;

        User bakery = userRepository.save(User.builder()
                .name("Artisan Crust Bakery")
                .email("contact@artisancrust.com")
                .role(UserRole.ROLE_VENDOR)
                .address("124 Market Street, Downtown")
                .phone("+1-555-0199")
                .latitude(37.7749)
                .longitude(-122.4194)
                .build());

        User shelter = userRepository.save(User.builder()
                .name("Hope Community Kitchen")
                .email("coordinator@hopekitchen.org")
                .role(UserRole.ROLE_SHELTER)
                .address("500 4th Street, Downtown")
                .phone("+1-555-0244")
                .latitude(37.7810)
                .longitude(-122.4010)
                .build());

        // 1. Normal sale (>24h)
        foodItemRepository.save(FoodItem.builder()
                .name("Baguettes & Brioche Loaves")
                .category(FoodCategory.BAKERY)
                .quantityKg(15.0)
                .originalPrice(BigDecimal.valueOf(45.00))
                .currentPrice(BigDecimal.valueOf(45.00))
                .expirationTime(LocalDateTime.now().plusHours(30))
                .vendor(bakery)
                .build());

        // 2. Critical donation item (expires in 3 hours)
        foodItemRepository.save(FoodItem.builder()
                .name("Prepared Chicken & Rice Bowls")
                .category(FoodCategory.PREPARED_MEALS)
                .quantityKg(25.0)
                .originalPrice(BigDecimal.valueOf(150.00))
                .currentPrice(BigDecimal.valueOf(150.00))
                .expirationTime(LocalDateTime.now().plusHours(3))
                .vendor(bakery)
                .build());

        System.out.println(">>> Sample seed data created with Lombok!");
    }
}