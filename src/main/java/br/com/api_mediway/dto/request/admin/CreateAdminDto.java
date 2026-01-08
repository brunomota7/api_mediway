package br.com.api_mediway.dto.request.admin;

public record CreateAdminDto(String name,
                             String email,
                             String number,
                             String password) {
}