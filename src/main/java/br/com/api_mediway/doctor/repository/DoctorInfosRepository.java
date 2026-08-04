package br.com.api_mediway.doctor.repository;

import br.com.api_mediway.doctor.entity.DoctorInfos;
import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorInfosRepository extends JpaRepository<DoctorInfos, Long> {
    Optional<DoctorInfos> findByUserUserId(UUID userId);
    Optional<DoctorInfos> findByCrm(String crm);
    List<DoctorInfos> findBySpecialtyIgnoreCase(String specialty);
    List<DoctorInfos> findByAvailability(DoctorAndCaregiverAvailability availability);
}
