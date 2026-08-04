package br.com.api_mediway.common.exception;

public class SlotAlreadyExistsException extends RuntimeException {
    public SlotAlreadyExistsException(String message) {
        super(message);
    }
}
