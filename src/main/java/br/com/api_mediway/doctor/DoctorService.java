package br.com.api_mediway.doctor;

import br.com.api_mediway.doctor.dto.request.AddInfosDoctorDTO;
import br.com.api_mediway.doctor.dto.request.UpdateDoctorInfosDTO;
import br.com.api_mediway.doctor.dto.response.DoctorResponseDTO;
import br.com.api_mediway.doctor.entity.DoctorInfos;
import br.com.api_mediway.user.entity.User;
import br.com.api_mediway.common.exception.UserNotFoundException;
import br.com.api_mediway.doctor.factory.DoctorFactory;
import br.com.api_mediway.user.factory.UserFactory;
import br.com.api_mediway.doctor.mapper.DoctorMapper;
import br.com.api_mediway.doctor.repository.DoctorInfosRepository;
import br.com.api_mediway.user.repository.UserRepository;
import br.com.api_mediway.common.util.UtilsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DoctorService {

    private final UserRepository userRepository;
    private final DoctorInfosRepository doctorInfosRepository;

    public DoctorService(UserRepository userRepository,
                         DoctorInfosRepository doctorInfosRepository) {
        this.userRepository = userRepository;
        this.doctorInfosRepository = doctorInfosRepository;
    }

    // registra as informações complementares do médico
    @Transactional
    public void registerInfosDoctor(AddInfosDoctorDTO dto, JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        if (userId == null) {
            log.error("[DOCTOR] User ID not found in token during registration attempt");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id of user not found");
        }

        log.info("[DOCTOR] Starting doctor info registration process for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("[DOCTOR] User not found for userId={}", userId);
                    return new UserNotFoundException("User not found");
                });

        DoctorInfos doctorInfos = DoctorFactory.createDoctorInfos(user, dto);
        doctorInfosRepository.save(doctorInfos);

        log.info("[DOCTOR] Doctor infos registered successfully for userEmail={}", user.getEmail());
    }

    @PreAuthorize("hasAuthority('SCOPE_MEDICO')")
    public DoctorResponseDTO getMyInfos(JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        if (userId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID not found in token");

        log.info("[DOCTOR] Fetching infos for authenticated userId={}", userId);
        DoctorInfos doctor = doctorInfosRepository.findByUserUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));

        return DoctorMapper.toResponse(doctor);
    }

    // busca as informações de um médico pelo CRM
    public DoctorResponseDTO getDoctorByCrm(String crm) {
        log.info("[DOCTOR] Searching for doctor with CRM={}", crm);
        DoctorInfos doctor = doctorInfosRepository.findByCrm(crm)
                .orElseThrow(() -> {
                    log.warn("[DOCTOR] Doctor not found with CRM={}", crm);
                    return new UserNotFoundException("Doctor not found");
                });
        log.info("[DOCTOR] Doctor found successfully with CRM={}", crm);
        return DoctorMapper.toResponse(doctor);
    }

    // retorna todos os médicos cadastrados
    public List<DoctorResponseDTO> getAllDoctors() {
        log.info("[DOCTOR] Fetching all registered doctors");
        List<DoctorResponseDTO> doctors = doctorInfosRepository.findAll()
                .stream()
                .map(DoctorMapper::toResponse)
                .toList();
        log.info("[DOCTOR] Found {} doctors registered in the system", doctors.size());
        return doctors;
    }

    // atualiza as informações do médico autenticado
    @Transactional
    public void updateInfosDoctor(JwtAuthenticationToken token, UpdateDoctorInfosDTO dto) {
        UUID doctorId = UtilsService.getUserIdFromToken(token);
        if (doctorId == null) {
            log.error("[DOCTOR] User ID not found in token during update attempt");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID not found in token");
        }

        log.info("[DOCTOR] Starting update of doctor infos for userId={}", doctorId);

        User user = userRepository.findById(doctorId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        DoctorInfos doctor = doctorInfosRepository.findByUserUserId(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));

        UserFactory.updateUserDoctor(user, dto);
        DoctorFactory.updateDoctorInfos(doctor, dto);

        userRepository.save(user);
        doctorInfosRepository.save(doctor);

        log.info("[DOCTOR] Doctor infos updated successfully for userId={}", doctorId);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteInfosDoctor(UUID doctorId) {
        DoctorInfos doctorInfos = doctorInfosRepository.findByUserUserId(doctorId)
                .orElseThrow(() -> new UserNotFoundException("Doctor infos not found"));

        log.info("[DOCTOR] Starting delete of doctor infos for userId={}", doctorId);

        doctorInfosRepository.delete(doctorInfos);
        log.info("[DOCTOR] Doctor infos delete successfully for userId={}", doctorId);
    }

}
