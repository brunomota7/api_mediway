package br.com.api_mediway.auth;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// limita, por IP, as tentativas nos endpoints do fluxo de reset de senha
// para dificultar brute-force do codigo de 6 digitos (ver C5 da revisao arquitetural)
@Component
public class PasswordResetRateLimiter {

    private static final int REQUEST_RESET_CAPACITY = 5;
    private static final Duration REQUEST_RESET_REFILL_PERIOD = Duration.ofHours(1);

    private static final int VALIDATE_CODE_CAPACITY = 5;
    private static final Duration VALIDATE_CODE_REFILL_PERIOD = Duration.ofMinutes(1);

    private final ConcurrentMap<String, Bucket> requestResetBuckets = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Bucket> validateCodeBuckets = new ConcurrentHashMap<>();

    // POST /auth/request-reset: limita geracao/envio de codigos por IP
    public boolean tryConsumeRequestReset(String clientIp) {
        return requestResetBuckets
                .computeIfAbsent(clientIp, ip -> newBucket(REQUEST_RESET_CAPACITY, REQUEST_RESET_REFILL_PERIOD))
                .tryConsume(1);
    }

    // POST /auth/validate-code: limita tentativas de adivinhar o codigo por IP
    public boolean tryConsumeValidateCode(String clientIp) {
        return validateCodeBuckets
                .computeIfAbsent(clientIp, ip -> newBucket(VALIDATE_CODE_CAPACITY, VALIDATE_CODE_REFILL_PERIOD))
                .tryConsume(1);
    }

    private Bucket newBucket(int capacity, Duration refillPeriod) {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(capacity).refillGreedy(capacity, refillPeriod))
                .build();
    }
}