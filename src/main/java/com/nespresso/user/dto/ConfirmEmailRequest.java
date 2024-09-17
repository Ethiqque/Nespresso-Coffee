package com.nespresso.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ConfirmEmailRequest(
        @NotBlank(message = "Token cannot be empty")
        String token
) {
}
