package br.com.api_mediway.auth.dto.response;

import java.io.Serializable;

public record ResetCodeMessage(String identifier, String code, String name) implements Serializable {
}
