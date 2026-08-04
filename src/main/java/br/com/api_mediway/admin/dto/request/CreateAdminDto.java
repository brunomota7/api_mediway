package br.com.api_mediway.admin.dto.request;

public record CreateAdminDto(String name,
                             String email,
                             String number,
                             String password) {
}