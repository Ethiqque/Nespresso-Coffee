package com.nespresso.payment.entity;

import com.nespresso.payment.enums.StripeSessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_id", nullable = false, unique = true)
    private String sessionId;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StripeSessionStatus status;

    @Column(name = "description")
    private String description;
}
