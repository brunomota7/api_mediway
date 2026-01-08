package br.com.api_mediway.dto.response.auth;

import java.io.Serializable;

public record ResetCodeMessage(String identifier, String code, String name) implements Serializable {
}
