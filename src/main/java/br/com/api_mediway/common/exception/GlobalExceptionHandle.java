package br.com.api_mediway.common.exception;

import br.com.api_mediway.common.exception.ErrorResponseDTO;
import br.com.api_mediway.common.exception.CannotDeleteAdminException;
import br.com.api_mediway.common.exception.CannotRemoveAdminRoleException;
import br.com.api_mediway.common.exception.EmailSendException;
import br.com.api_mediway.common.exception.SlotAlreadyExistsException;
import br.com.api_mediway.common.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {

    // ========== 404 - Recurso não encontrado ==========
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "User not found", ex.getMessage(), request);
    }

    // ========== 403 - Regras de negócio que proíbem a ação ==========
    @ExceptionHandler({CannotRemoveAdminRoleException.class, CannotDeleteAdminException.class})
    public ResponseEntity<ErrorResponseDTO> handleForbiddenBusinessRule(RuntimeException ex, WebRequest request) {
        log.warn("[SECURITY] Forbidden action blocked - {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden action", ex.getMessage(), request);
    }

    // ========== 409 - Conflito ==========
    @ExceptionHandler(SlotAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleSlotAlreadyExists(SlotAlreadyExistsException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, "Slot already exists", ex.getMessage(), request);
    }

    // ========== 500 - Falha de infraestrutura (envio de e-mail) ==========
    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailSendException(EmailSendException ex, WebRequest request) {
        log.error("[EMAIL] Failed to send email - {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Email delivery failed",
                "Não foi possível enviar o e-mail no momento. Tente novamente mais tarde.", request);
    }

    // ========== 401 - Falha de autenticação ==========
    // Cobre BadCredentialsException (login inválido) e qualquer outra AuthenticationException
    // (ex.: token JWT inválido/expirado). Antes desta correção essas exceções caíam no handler
    // genérico e retornavam 500 ao cliente em vez de 401.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        log.warn("[SECURITY] Authentication failed for request {} - {}", request.getDescription(false), ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Authentication failed",
                "Credenciais inválidas ou sessão expirada", request);
    }

    // ========== 403 - Falha de autorização (@PreAuthorize negado) ==========
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        log.warn("[SECURITY] Access denied for request {}", request.getDescription(false));
        return buildResponse(HttpStatus.FORBIDDEN, "Access denied",
                "Você não tem permissão para executar essa ação", request);
    }

    // ========== 400 - Falha de validação (@Valid em @RequestBody) ==========
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", message, request);
    }

    // ========== Exceções explícitas via ResponseStatusException (usadas nos services) ==========
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return buildResponse(status, status.getReasonPhrase(), ex.getReason(), request);
    }

    // ========== Fallback - nunca vaza detalhes internos ao cliente ==========
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, WebRequest request) {
        log.error("[UNHANDLED] Unexpected error on request {}", request.getDescription(false), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.", request);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String error, String message, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ErrorResponseDTO body = new ErrorResponseDTO(Instant.now(), status.value(), error, message, path);
        return ResponseEntity.status(status).body(body);
    }
}
