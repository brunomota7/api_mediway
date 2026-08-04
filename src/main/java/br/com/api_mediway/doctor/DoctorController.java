package br.com.api_mediway.doctor;

import br.com.api_mediway.doctor.dto.request.AddInfosDoctorDTO;
import br.com.api_mediway.doctor.dto.request.UpdateDoctorInfosDTO;
import br.com.api_mediway.doctor.dto.response.DoctorResponseDTO;
import br.com.api_mediway.doctor.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // cadastra as informações do médico autenticado
    @PostMapping("/add-infos")
    @PreAuthorize("hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> registerInfosDoctor(@RequestBody AddInfosDoctorDTO dto,
                                                    JwtAuthenticationToken token) {
        log.info("[Doctor] POST /doctor/add-infos - Starting doctor information registration");
        doctorService.registerInfosDoctor(dto, token);
        log.info("[Doctor] POST /doctor/add-infos - Doctor information successfully registered");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // retorna as informações do médico autenticado
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<DoctorResponseDTO> getMyInfos(JwtAuthenticationToken token) {
        log.info("[DOCTOR] GET /doctor/me - Fetching authenticated doctor infos");
        var response = doctorService.getMyInfos(token);
        log.info("[DOCTOR] GET /doctor/me - Infos fetched successfully for authenticated user");
        return ResponseEntity.ok(response);
    }

    // retorna as informações de um médico pelo CRM
    @GetMapping("/{crm}/crm")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<DoctorResponseDTO> getDoctorByCrm(
            @PathVariable String crm
    ) {
        log.info("[DOCTOR] GET /doctor/{crm}/crm - Fetching doctor infos by CRM");
        var response = doctorService.getDoctorByCrm(crm);
        log.info("[DOCTOR] GET /doctor/{crm}/crm - Infos fetched successfully for doctor for crm = {}", crm);
        return ResponseEntity.ok(response);
    }

    // lista todos os médicos do sistema
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<DoctorResponseDTO>> getAll() {
        log.info("[DOCTOR] GET /doctor/all - Fetching all doctor");
        List<DoctorResponseDTO> response = doctorService.getAllDoctors();
        log.info("[DOCTOR] GET /doctor/all - Found {} doctors", response.size());
        return ResponseEntity.ok(response);
    }

    // atualiza as informações do médico autenticado
    @PutMapping("/update-infos")
    @PreAuthorize("hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> updateInfosDoctor(
            @RequestBody UpdateDoctorInfosDTO dto,
            JwtAuthenticationToken token
    ) {
        log.info("[DOCTOR] PUT /doctor/update-infos - Updating doctor infos");
        doctorService.updateInfosDoctor(token, dto);
        log.info("[DOCTOR] PUT /doctor/update-infos - Infos updated successfully");
        return ResponseEntity.ok().build();
    }

    // Deleta informações de um médico pelo ID
    @DeleteMapping("/{doctorId}/delete")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteInfosDoctor(
            @PathVariable UUID doctorId
    ) {
        log.info("[DOCTOR] DELETE /doctor/{doctorId}/delete - Starting delete doctor infos");
        doctorService.deleteInfosDoctor(doctorId);
        log.info("[DOCTOR] DELETE /doctor/{doctorId}/delete - Delete doctor infos for ID: {}", doctorId);
        return ResponseEntity.ok().build();
    }

}
