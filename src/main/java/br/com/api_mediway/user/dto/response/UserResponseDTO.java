package br.com.api_mediway.user.dto.response;

import br.com.api_mediway.user.entity.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(UUID userId,
                              String name,
                              String email,
                              String number,
                              Set<Role> roles) {
}
