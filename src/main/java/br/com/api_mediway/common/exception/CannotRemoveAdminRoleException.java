package br.com.api_mediway.common.exception;

public class CannotRemoveAdminRoleException extends RuntimeException {
    public CannotRemoveAdminRoleException(String message) {
        super(message);
    }
}
