package br.com.api_mediway.repository;

import br.com.api_mediway.entites.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByCodeAndUser_EmailAndUsedFalse(String code, String email);
    Optional<PasswordResetCode> findByCodeAndUser_NumberAndUsedFalse(String code, String number);
    Optional<PasswordResetCode> findByCodeAndUsedFalse(String code);
    Optional<PasswordResetCode> findTopByUser_UserIdOrderByExpirationDesc(UUID userId);
}
