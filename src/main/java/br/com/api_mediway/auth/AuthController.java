package br.com.api_mediway.auth;

import br.com.api_mediway.auth.dto.request.*;
import br.com.api_mediway.auth.dto.response.LoginResponseDTO;
import br.com.api_mediway.auth.dto.response.TokenTempResetDTO;
import br.com.api_mediway.auth.AuthService;
import br.com.api_mediway.auth.PasswordResetCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetCodeService passwordResetCodeService;

    public AuthController(AuthService authService,
                          PasswordResetCodeService passwordResetCodeService) {
        this.authService = authService;
        this.passwordResetCodeService = passwordResetCodeService;
    }

    // registro de novo usuário
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody CreateUserDTO dto) {
        log.info("[AUTH] POST /auth/register - Starting registration for email={}", dto.email());
        authService.toRegister(dto);
        log.info("[AUTH] POST /auth/register - User successfully registered with email={}", dto.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // login do usuário
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        log.info("[AUTH] POST /auth/login - Starting login for email={}", dto.email());
        var response = authService.toLogin(dto);
        log.info("[AUTH] POST /auth/login - Login successful for email={}", dto.email());
        return ResponseEntity.ok(response);
    }

    // solicitação de redefinição de senha
    @PostMapping("/request-reset")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody PasswordResetRequestDTO dto) {
        log.info("[AUTH] POST /auth/request-reset - Starting password reset request for identifier={}", dto.identifier());
        authService.requestPasswordReset(dto.identifier());
        log.info("[AUTH] POST /auth/request-reset - Password reset code sent successfully");
        return ResponseEntity.ok().build();
    }

    // valida o código de redefinição de senha
    @PostMapping("/validate-code")
    public ResponseEntity<TokenTempResetDTO> validateResetCode(@RequestBody PasswordResetCodeDTO dto) {
        log.info("[AUTH] POST /auth/validate-code - Validating password reset code");
        var tempToken = passwordResetCodeService.validateResetCode(dto.code());
        log.info("[AUTH] POST /auth/validate-code - Code validated successfully, temporary token generated");
        return ResponseEntity.ok(tempToken);
    }

    // redefine a senha usando o token temporário
    @PreAuthorize("hasAuthority('SCOPE_RESET')")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestBody PasswordResetConfirmDTO dto,
            JwtAuthenticationToken token
    ) {
        log.info("[AUTH] POST /auth/reset-password - Starting password reset process");
        passwordResetCodeService.resetPassword(dto.newPassword(), token);
        log.info("[AUTH] POST /auth/reset-password - Password successfully reset");
        return ResponseEntity.ok().build();
    }
}