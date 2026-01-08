package br.com.api_mediway.dto.request.auth;

public record CreateUserDTO(String name,
                            String email,
                            String number,
                            String password,
                            String role) {
}
