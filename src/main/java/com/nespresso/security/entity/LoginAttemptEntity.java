package com.nespresso.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "login_attempts")
public class LoginAttemptEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "attempts", nullable = false)
    private Integer attempts;

    @Column(name = "expiration_datetime", nullable = true)
    private LocalDateTime expirationDatetime;

    @Column(name = "is_user_locked", nullable = false)
    private Boolean isUserLocked;

    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified;
}
