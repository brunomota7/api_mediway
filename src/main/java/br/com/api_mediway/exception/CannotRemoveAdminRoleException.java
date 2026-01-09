package br.com.api_mediway.exception;

public class CannotRemoveAdminRoleException extends RuntimeException {
    public CannotRemoveAdminRoleException(String message) {
        super(message);
    }
}
