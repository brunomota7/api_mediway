package br.com.api_mediway.dto.response.user;

import br.com.api_mediway.entites.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(UUID userId,
                              String name,
                              String email,
                              String number,
                              Set<Role> roles) {
}
