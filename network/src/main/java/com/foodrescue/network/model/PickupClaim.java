package com.foodrescue.network.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PickupClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "food_item_id", nullable = false, unique = true)
    private FoodItem foodItem;

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private User shelter;

    @Builder.Default
    private LocalDateTime claimTime = LocalDateTime.now();

    private LocalDateTime pickupDeadline;
    private LocalDateTime completedTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ClaimStatus status = ClaimStatus.PENDING_PICKUP;

    private String notes;

}
