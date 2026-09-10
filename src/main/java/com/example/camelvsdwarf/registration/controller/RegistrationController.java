package com.example.camelvsdwarf.registration.controller;

import com.example.camelvsdwarf.registration.RegistrationRequest;
import com.example.camelvsdwarf.registration.RegistrationResponse;
import com.example.camelvsdwarf.registration.RegistrationStatus;
import com.example.camelvsdwarf.registration.RegistrationRequest;
import com.example.camelvsdwarf.registration.service.RegistrationService;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    //Atributos
    private final RegistrationService registrationService;

    //Obtener todas las inscripciones
    @GetMapping
    public ResponseEntity<PageResponse<RegistrationResponse>> getAllRegistrations(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) RegistrationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(registrationService.getAllRegistrations(query, status, page, size, sortBy, sortDir));
    }

    //Obtener inscripción por ID
    @GetMapping("/{id}")
    public ResponseEntity<RegistrationResponse> getRegistrationById(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getRegistrationById(id));
    }

    //Crear inscripción
    @PostMapping
    public ResponseEntity<RegistrationResponse> createRegistration(@Valid @RequestBody RegistrationRequest registrationRequest) {
        RegistrationResponse registrationResponse = registrationService.createRegistration(registrationRequest);
        return ResponseEntity.created(URI.create("/api/v1/registrations/" + registrationResponse.getId())).body(registrationResponse);
    }

    //Actualizar inscripción
    @PutMapping("/{id}")
    public ResponseEntity<RegistrationResponse> updateRegistration(@PathVariable Long id, @Valid @RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(registrationService.updateRegistration(id, registrationRequest));
    }

    //Actualizar estado de inscripción
    @PatchMapping("/{id}/status")
    public ResponseEntity<RegistrationResponse> updateRegistrationStatus(@PathVariable Long id, @Valid @RequestBody RegistrationStatus status) {
        return ResponseEntity.ok(registrationService.updateRegistrationStatus(id, status));
    }

    // Tomar decisión / Aprobar o Rechazar inscripción
    @PatchMapping("/{id}/decision")
    public ResponseEntity<RegistrationResponse> evaluateRegistration(
            @PathVariable Long id,
            @Valid @RequestBody RegistrationDecisionRequest decisionRequest) {
        return ResponseEntity.ok(registrationService.evaluateRegistration(id, decisionRequest));
    }

    //Eliminar inscripción
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }

}
