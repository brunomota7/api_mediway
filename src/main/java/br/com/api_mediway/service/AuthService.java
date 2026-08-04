package br.com.api_mediway.service;

import br.com.api_mediway.dto.request.auth.CreateUserDTO;
import br.com.api_mediway.dto.request.auth.LoginRequestDTO;
import br.com.api_mediway.dto.response.auth.LoginResponseDTO;
import br.com.api_mediway.dto.response.auth.ResetCodeMessage;
import br.com.api_mediway.entites.PasswordResetCode;
import br.com.api_mediway.entites.Role;
import br.com.api_mediway.entites.User;
import br.com.api_mediway.exception.UserNotFoundException;
import br.com.api_mediway.repository.PasswordResetCodeRepository;
import br.com.api_mediway.repository.RoleRepository;
import br.com.api_mediway.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final RabbitTemplate rabbitTemplate;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       JwtTokenService jwtTokenService,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder,
                       PasswordResetCodeRepository passwordResetCodeRepository,
                       RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.passwordResetCodeRepository = passwordResetCodeRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // registra um novo usuário na plataforma
    @Transactional
    public void toRegister(CreateUserDTO dto) {
        log.info("[AUTH] Starting new user registration process for email={}", dto.email());

        var existingUser = userRepository.findByEmail(dto.email());
        if (existingUser.isPresent()) {
            log.warn("[AUTH] Attempt to register existing email={}", dto.email());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exists");
        }

        Role userRole = roleRepository.findByName(dto.role());
        if (userRole == null) {
            log.error("[AUTH] Role '{}' not found for registration", dto.role());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found");
        }

        if (userRole.getRoleId().equals(Role.Values.ADMIN.getRoleId())) {
            log.warn("[AUTH] Attempt to register with unauthorized role={}", dto.role());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role for registration");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setNumber(dto.number());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
        log.info("[AUTH] User registered successfully with email={}", dto.email());
    }

    // realiza login e gera token JWT
    public LoginResponseDTO toLogin(LoginRequestDTO dto) {
        log.info("[AUTH] Attempting login for email={}", dto.email());

        var userOpt = userRepository.findByEmail(dto.email());
        if (userOpt.isEmpty() || !userOpt.get().isLoginCorrect(dto, passwordEncoder)) {
            log.warn("[AUTH] Login failed for email={} - Invalid credentials", dto.email());
            throw new BadCredentialsException("User or password is invalid!");
        }

        User user = userOpt.get();
        Long expiresIn = 172800L; // 2 dias
        String token = jwtTokenService.generateToken(user, expiresIn);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        log.info("[AUTH] Login successful for userId={}", user.getUserId());
        return new LoginResponseDTO(token, expiresIn, roles);
    }

    // gera e envia código de redefinição de senha
    public void requestPasswordReset(String identifier) {
        log.info("[AUTH] Password reset requested for identifier={}", identifier);

        Optional<User> userOpt = identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                : userRepository.findByNumber(identifier);

        User user = userOpt.orElseThrow(() -> new UserNotFoundException("User not found"));

        String code = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
        Instant expiration = Instant.now().plus(Duration.ofMinutes(3));

        PasswordResetCode resetCode = new PasswordResetCode();
        resetCode.setCode(code);
        resetCode.setExpiration(expiration);
        resetCode.setUsed(false);
        resetCode.setUser(user);

        passwordResetCodeRepository.save(resetCode);
        sendResetCode(identifier, code, user.getName());

        log.info("[AUTH] Password reset code generated for userId={} and sent to {}", user.getUserId(), identifier);
    }

    // envia o código de redefinição via fila RabbitMQ
    public void sendResetCode(String identifier, String code, String name) {
        ResetCodeMessage message = new ResetCodeMessage(identifier, code, name);
        rabbitTemplate.convertAndSend("reset-code-exchange", "reset.code", message);
        log.info("[AUTH] Reset code message sent to queue for identifier={}", identifier);
    }
}