package com.foodrescue.network.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.foodrescue.network.model.FoodCategory;
import com.foodrescue.network.model.ListingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemResponseDTO {

    private Long id;
    private String name;
    private FoodCategory category;
    private Double quantityKg;
    private BigDecimal originalPrice;
    private BigDecimal currentPrice;
    private LocalDateTime expirationTime;
    private ListingStatus status;
    private Long vendorId;
    private String vendorName;
    private String vendorAddress;

}
