package br.com.api_mediway.auth;

import br.com.api_mediway.auth.dto.response.TokenTempResetDTO;
import br.com.api_mediway.auth.entity.PasswordResetCode;
import br.com.api_mediway.user.entity.User;
import br.com.api_mediway.common.exception.UserNotFoundException;
import br.com.api_mediway.auth.repository.PasswordResetCodeRepository;
import br.com.api_mediway.user.repository.UserRepository;
import br.com.api_mediway.common.util.UtilsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class PasswordResetCodeService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordResetCodeRepository passwordResetCodeRepository;

    public PasswordResetCodeService(UserRepository userRepository,
                                    JwtTokenService jwtTokenService,
                                    BCryptPasswordEncoder passwordEncoder,
                                    PasswordResetCodeRepository passwordResetCodeRepository) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetCodeRepository = passwordResetCodeRepository;
    }

    /**
     * valida o código de redefinição de senha e gera um token temporário
     * válido por 5 minutos
     */
    public TokenTempResetDTO validateResetCode(String code) {
        log.info("[RESET] Validating password reset code={}", code);

        PasswordResetCode token = passwordResetCodeRepository.findByCodeAndUsedFalse(code)
                .orElseThrow(() -> {
                    log.warn("[RESET] Invalid or already used code={}", code);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired code");
                });

        if (token.getExpiration().isBefore(Instant.now())) {
            log.warn("[RESET] Expired code={} for user={}", code, token.getUser().getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expired code");
        }

        User user = token.getUser();
        TokenTempResetDTO tempToken = new TokenTempResetDTO(jwtTokenService.generateResetToken(user, 300L));

        log.info("[RESET] Code successfully validated for user={}", user.getEmail());
        return tempToken;
    }

    // redefine a senha do usuário autenticado via token temporário
    @Transactional
    public void resetPassword(String newPassword, JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        log.info("[RESET] Initiating password reset for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("[RESET] User not found for userId={}", userId);
                    return new UserNotFoundException("User not found");
                });

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("[RESET] Password updated successfully for user={}", user.getEmail());

        PasswordResetCode resetCode = passwordResetCodeRepository.findTopByUser_UserIdOrderByExpirationDesc(userId)
                .orElseThrow(() -> {
                    log.error("[RESET] No reset code found for userId={}", userId);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code not found");
                });

        resetCode.setUsed(true);
        passwordResetCodeRepository.save(resetCode);
        log.info("[RESET] Code marked as used for user={}", user.getEmail());

        log.info("[RESET] Password reset process completed successfully for user={}", user.getEmail());
    }
}

