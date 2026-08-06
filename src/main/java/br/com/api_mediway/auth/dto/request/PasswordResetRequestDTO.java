package br.com.api_mediway.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequestDTO(@NotBlank String identifier) {
}