package br.com.api_mediway.common.exception;

import java.time.Instant;

/**
 * Formato padrão de erro devolvido por toda a API.
 * Mantém a mesma forma para qualquer exceção tratada pelo GlobalExceptionHandle.
 */
public record ErrorResponseDTO(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
