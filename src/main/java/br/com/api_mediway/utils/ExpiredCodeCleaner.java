package br.com.api_mediway.utils;

import br.com.api_mediway.entites.PasswordResetCode;
import br.com.api_mediway.repository.PasswordResetCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class ExpiredCodeCleaner {

    private final PasswordResetCodeRepository repository;

    public ExpiredCodeCleaner(PasswordResetCodeRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60000)
    public void clearExpiredCodes() {
        Instant now = Instant.now();
        List<PasswordResetCode> expired = repository.findAll()
                .stream()
                .filter(code -> code.getExpiration().isBefore(now) && !code.isUsed())
                .toList();

        repository.deleteAll(expired);
        log.info("Expired codes cleaned: {}", expired.size());
    }

}
