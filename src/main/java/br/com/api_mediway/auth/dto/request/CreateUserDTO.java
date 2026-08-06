package br.com.api_mediway.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(@NotBlank String name,
                            @NotBlank @Email String email,
                            @NotBlank String number,
                            @NotBlank @Size(min = 8) String password,
                            @NotBlank String role) {
}