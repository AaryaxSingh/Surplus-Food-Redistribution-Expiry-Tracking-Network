package com.foodrescue.network.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodrescue.network.dto.FoodItemRequestDTO;
import com.foodrescue.network.dto.FoodItemResponseDTO;
import com.foodrescue.network.service.FoodItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/food-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FoodItemController {

    private final FoodItemService foodItemService;

    @PostMapping
    public ResponseEntity<FoodItemResponseDTO> createFoodItem(@Valid @RequestBody FoodItemRequestDTO request) {
        FoodItemResponseDTO created = foodItemService.createFoodItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<FoodItemResponseDTO>> getAllAvailable() {
        return ResponseEntity.ok(foodItemService.getAllActiveItems());
    }

    @GetMapping("/donations")
    public ResponseEntity<List<FoodItemResponseDTO>> getDonations() {
        return ResponseEntity.ok(foodItemService.getDonationCriticalItems());
    }

}
