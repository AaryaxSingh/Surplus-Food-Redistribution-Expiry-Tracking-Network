package com.foodrescue.network.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodrescue.network.dto.ClaimRequestDTO;
import com.foodrescue.network.dto.ClaimResponseDTO;
import com.foodrescue.network.service.ClaimService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    public ResponseEntity<ClaimResponseDTO> claimDonation(@Valid @RequestBody ClaimRequestDTO req) {
        ClaimResponseDTO claim = claimService.claimDonation(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(claim);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ClaimResponseDTO> completePickup(@PathVariable Long id) {
        ClaimResponseDTO completed = claimService.completePickup(id);
        return ResponseEntity.ok(completed);
    }
}
