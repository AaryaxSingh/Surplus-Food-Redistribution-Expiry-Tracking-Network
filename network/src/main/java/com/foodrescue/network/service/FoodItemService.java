package com.foodrescue.network.service;

import com.foodrescue.network.model.FoodCategory;
import com.foodrescue.network.model.FoodItem;
import com.foodrescue.network.model.ListingStatus;
import com.foodrescue.network.model.User;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodrescue.network.dto.FoodItemRequestDTO;
import com.foodrescue.network.dto.FoodItemResponseDTO;
import com.foodrescue.network.repository.FoodItemRepository;
import com.foodrescue.network.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public FoodItemResponseDTO createFoodItem(FoodItemRequestDTO req) {
        User vendor = userRepository.findById(req.getVendorId())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found:" + req.getVendorId()));
        FoodItem item = FoodItem.builder()
                .name(req.getName())
                .category(FoodCategory.valueOf(req.getCategory().toUpperCase()))
                .quantityKg(req.getQuantityKg())
                .originalPrice(req.getOriginalPrice())
                .currentPrice(req.getOriginalPrice())
                .expirationTime(req.getExpirationTime())
                .status(ListingStatus.ACTIVE_SALE)
                .vendor(vendor)
                .build();

        FoodItem saved = foodItemRepository.save(item);
        return mapToDTO(saved);

    }

    @Transactional(readOnly = true)
    public List<FoodItemResponseDTO> getAllActiveItems() {
        return foodItemRepository.findAllActiveForDecayEvaluation().stream().map(this::mapToDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<FoodItemResponseDTO> getDonationCriticalItems() {
        return foodItemRepository.findByStatus(ListingStatus.DONATION_CRITICAL).stream()
                .map(this::mapToDTO)
                .toList();
    }

    public FoodItemResponseDTO mapToDTO(FoodItem item) {
        return FoodItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .category(item.getCategory())
                .quantityKg(item.getQuantityKg())
                .originalPrice(item.getOriginalPrice())
                .currentPrice(item.getCurrentPrice())
                .expirationTime(item.getExpirationTime())
                .status(item.getStatus())
                .vendorId(item.getVendor().getId())
                .vendorName(item.getVendor().getName())
                .vendorAddress(item.getVendor().getAddress())
                .build();

    }
}
