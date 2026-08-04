package br.com.api_mediway.caregiver.repository;

import br.com.api_mediway.caregiver.entity.CaregiverInfos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CaregiverInfosRepository extends JpaRepository<CaregiverInfos, Long> {
    Optional<CaregiverInfos> findByUserUserId(UUID userId);
    boolean existsByUserUserId(UUID userId);
}
