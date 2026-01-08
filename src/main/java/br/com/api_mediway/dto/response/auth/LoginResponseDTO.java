package br.com.api_mediway.dto.response.auth;

import java.util.Set;

public record LoginResponseDTO(String accessToken, Long expiresIn, Set<String> roles) {
}
