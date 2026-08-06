package br.com.api_mediway.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirmDTO(@NotBlank @Size(min = 8) String newPassword) {
}