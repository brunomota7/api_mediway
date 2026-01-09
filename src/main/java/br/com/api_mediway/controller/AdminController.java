package br.com.api_mediway.controller;

import br.com.api_mediway.dto.request.admin.CreateAdminDto;
import br.com.api_mediway.dto.response.user.UserResponseDTO;
import br.com.api_mediway.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // registro de novo administrador
    @PostMapping("/register-admin")
    public ResponseEntity<Void> registerAdmin(@RequestBody CreateAdminDto dto) {
        log.info("[ADMIN] POST /admin/register-admin - Starting registration for new admin email={}", dto.email());
        adminService.createUserAdmin(dto);
        log.info("[ADMIN] POST /admin/register-admin - Admin registered successfully email={}", dto.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // remoção de permissão de administrador
    @DeleteMapping("/remove/{userId}")
    public ResponseEntity<Void> removeAdminRole(
            @PathVariable UUID userId,
            JwtAuthenticationToken token
    ) {
        log.warn("[ADMIN] DELETE /admin/remove/{} - Starting removal of admin role", userId);
        adminService.removeAdminRole(userId, token);
        log.info("[ADMIN] DELETE /admin/remove/{} - Admin role removed successfully", userId);
        return ResponseEntity.noContent().build();
    }

    // buscar usuário por ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID userId) {
        log.info("[ADMIN] GET /admin/user/{} - Fetching user data", userId);
        var response = adminService.getUserById(userId);
        log.info("[ADMIN] GET /admin/user/{} - User retrieved successfully", userId);
        return ResponseEntity.ok(response);
    }

    // listar todos os usuários
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("[ADMIN] GET /admin/users - Fetching all users");
        var users = adminService.getAllUsers();
        log.info("[ADMIN] GET /admin/users - Found {} users", users.size());
        return ResponseEntity.ok(users);
    }

    // excluir usuário definitivamente
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        log.warn("[ADMIN] DELETE /admin/user/{} - Starting deletion process", userId);
        adminService.deleteUser(userId);
        log.info("[ADMIN] DELETE /admin/user/{} - User deleted successfully", userId);
        return ResponseEntity.noContent().build();
    }
}
