package br.com.api_mediway.auth.dto.request;

public record CreateUserDTO(String name,
                            String email,
                            String number,
                            String password,
                            String role) {
}
