package com.foodrescue.network.controller;

import com.foodrescue.network.dto.ImpactSummaryDTO;
import com.foodrescue.network.service.ImpactReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/impact")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImpactController {

    private final ImpactReportingService impactReportingService;

    @GetMapping("/summary")
    public ResponseEntity<ImpactSummaryDTO> getImpactSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        // Default to all-time if no dates are provided
        if (startDate == null)
            startDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        if (endDate == null)
            endDate = LocalDateTime.now();

        ImpactSummaryDTO summary = impactReportingService.generateImpactMetrics(startDate, endDate);
        return ResponseEntity.ok(summary);
    }
}
