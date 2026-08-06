package br.com.api_mediway.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetCodeDTO(
        @NotBlank
        @Pattern(regexp = "^\\d{6}$", message = "code must be a 6-digit number")
        String code
) {
}
