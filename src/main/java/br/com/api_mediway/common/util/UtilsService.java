package br.com.api_mediway.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public class UtilsService {

    private UtilsService() {
        throw new UnsupportedOperationException("Utility class, cannot be instantiated.");
    }

    public static UUID getUserIdFromToken(JwtAuthenticationToken token) {
        String userIdStr = token.getToken().getClaimAsString("sub");
        if (userIdStr == null)
            throw new BadCredentialsException("Token does not contain user ID");

        try {
            return UUID.fromString(userIdStr);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid user ID format in token");
        }
    }

    public static String masEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        return parts[0].charAt(0) + "*****@" + parts[1];
    }

    // resolve o IP do cliente a partir da conexao TCP.
    // Nao confia em X-Forwarded-For pois a aplicacao nao roda atras de um proxy
    // reverso configurado como confiavel (server.forward-headers-strategy); um
    // atacante poderia forjar esse header para burlar o rate limiting.
    public static String getClientIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    public static String getEnumLabel(Enum<?> e) {
        try {
            var method = e.getClass().getMethod("getLabel");
            return (String) method.invoke(e);
        } catch (Exception ex) {
            return e.name();
        }
    }

}
