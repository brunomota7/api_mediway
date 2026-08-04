package br.com.api_mediway.auth.dto.response;

import java.util.Set;

public record LoginResponseDTO(String accessToken, Long expiresIn, Set<String> roles) {
}
