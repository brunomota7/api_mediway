package br.com.api_mediway.common.exception;

public class CannotDeleteAdminException extends RuntimeException {
    public CannotDeleteAdminException(String message) {
        super(message);
    }
}
