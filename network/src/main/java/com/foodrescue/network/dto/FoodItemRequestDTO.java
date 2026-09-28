package com.foodrescue.network.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodItemRequestDTO {

    @NotBlank(message = "food name is required")
    private String name;

    @NotNull(message = "food category is required")
    private String category;

    @NotNull(message = "quantity cannot be zero")
    @Positive(message = "quantity must be positive")
    private double quantityKg;

    @NotNull(message = "price cannot be zero")
    @Positive(message = "price must be positive")
    private BigDecimal originalPrice;

    @NotNull(message = "expiration time is required")
    private LocalDateTime expirationTime;

    @NotNull(message = "Vendor Id is required")
    private Long vendorId;

}
