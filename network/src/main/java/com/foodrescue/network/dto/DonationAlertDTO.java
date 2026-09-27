package com.foodrescue.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationAlertDTO {

    private Long fooditemid;
    private String foodName;
    private String category;
    private Double quantityKg;
    private String vendorName;
    private String vendorAddress;
    private String vendorPhone;
    private LocalDateTime expirationTime;
    private Long hoursRemaining;
    private String alertMessage;

}
