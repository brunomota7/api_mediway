package br.com.api_mediway.service;

import br.com.api_mediway.entites.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(User user, Long expiresIn) {
        var now = Instant.now();
        var claims = buildClaims(user, now, expiresIn);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateResetToken(User user, Long expiresIn) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("mediway-backend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", "RESET")
                .claim("username", user.getEmail())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String buildScopes(User user) {
        return user.getRoles()
                .stream()
                .map(role -> role.getName().toUpperCase())
                .collect(Collectors.joining(" "));
    }

    public JwtClaimsSet buildClaims(User user, Instant now, Long expiresIn) {
        return JwtClaimsSet.builder()
                .issuer("mediway-backend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", buildScopes(user))
                .claim("username", user.getEmail())
                .build();
    }

}
