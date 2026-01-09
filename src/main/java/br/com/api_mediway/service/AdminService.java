package br.com.api_mediway.service;

import br.com.api_mediway.dto.request.admin.CreateAdminDto;
import br.com.api_mediway.dto.response.user.UserResponseDTO;
import br.com.api_mediway.entites.Role;
import br.com.api_mediway.entites.User;
import br.com.api_mediway.exception.CannotDeleteAdminException;
import br.com.api_mediway.exception.CannotRemoveAdminRoleException;
import br.com.api_mediway.exception.UserNotFoundException;
import br.com.api_mediway.mapper.UserMapper;
import br.com.api_mediway.repository.RoleRepository;
import br.com.api_mediway.repository.UserRepository;
import br.com.api_mediway.utils.UtilsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository,
                        RoleRepository roleRepository,
                        BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUserAdmin(CreateAdminDto dto) {
        log.info("[ADMIN] Starting creation process for new admin email={}", dto.email());

        var existingUser = userRepository.findByEmail(dto.email());
        if (existingUser.isPresent()) {
            log.warn("[ADMIN] Attempt to register admin with existing email={}", dto.email());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        var adminRole = new Role();
        adminRole.setName(Role.Values.ADMIN.name());

        var user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setNumber(dto.number());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(adminRole));

        userRepository.save(user);
        log.info("[ADMIN] Admin user created successfully email={} id={}", user.getEmail(), user.getUserId());
    }

    @Transactional
    public void removeAdminRole(UUID targetUserId, JwtAuthenticationToken token) {
        log.info("[ADMIN] Removing admin role from userId={}", targetUserId);

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> {
                    log.error("[ADMIN] User not found with id={}", targetUserId);
                    return new UserNotFoundException("User not found");
                });

        UUID currentUserId = UtilsService.getUserIdFromToken(token);
        if (currentUserId.equals(targetUser.getUserId())) {
            log.warn("[ADMIN] User id={} attempted to remove their own admin role", currentUserId);
            throw new CannotRemoveAdminRoleException("You cannot remove your own admin role");
        }

        Role adminRole = roleRepository.findByName(Role.Values.ADMIN.name());
        if (!targetUser.getRoles().contains(adminRole)) {
            log.warn("[ADMIN] Attempt to remove admin role from non-admin userId={}", targetUserId);
            throw new CannotRemoveAdminRoleException("User is not an admin");
        }

        long totalAdmins = userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(adminRole))
                .count();
        if (totalAdmins <= 1) {
            log.warn("[ADMIN] Attempt to remove the last admin userId={}", targetUserId);
            throw new CannotRemoveAdminRoleException("Cannot remove the last admin");
        }

        targetUser.getRoles().remove(adminRole);
        userRepository.save(targetUser);
        log.info("[ADMIN] Admin role removed successfully from userId={}", targetUserId);
    }

    public List<UserResponseDTO> getAllUsers() {
        log.info("[ADMIN] Retrieving all users");
        var users = userRepository.findAll()
                .stream()
                .map(UserMapper::toInfosUser)
                .toList();
        log.info("[ADMIN] Found {} users", users.size());
        return users;
    }

    public UserResponseDTO getUserById(UUID userId) {
        log.info("[ADMIN] Retrieving user by id={}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("[ADMIN] User not found id={}", userId);
                    throw new UserNotFoundException("User not found");
                });
        log.info("[ADMIN] User retrieved successfully id={}", userId);
        return UserMapper.toInfosUser(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        log.warn("[ADMIN] Starting deletion process for userId={}", userId);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("[ADMIN] User not found for deletion id={}", userId);
                    throw new UserNotFoundException("User not found");
                });

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(Role.Values.ADMIN.name()));

        if (isAdmin) {
            log.warn("[ADMIN] Attempt to delete an admin userId={}", userId);
            throw new CannotDeleteAdminException("Admins cannot be deleted");
        }

        userRepository.delete(user);
        log.info("[ADMIN] User deleted successfully id={}", userId);
    }
}
