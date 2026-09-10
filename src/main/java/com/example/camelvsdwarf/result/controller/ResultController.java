package com.example.camelvsdwarf.result.controller;

import com.example.camelvsdwarf.result.ResultRequest;
import com.example.camelvsdwarf.result.ResultResponse;
import com.example.camelvsdwarf.result.ResultStatus;
import com.example.camelvsdwarf.result.ResultStatusRequest;
import com.example.camelvsdwarf.result.service.ResultService;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    // Obtener todos los resultados (con filtros por carrera, competidor y estado)
    @GetMapping
    public ResponseEntity<PageResponse<ResultResponse>> getAllResults(
            @RequestParam(required = false) Long raceId,
            @RequestParam(required = false) Long competitorId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) ResultStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "finalPosition") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(resultService.getAllResults(raceId, competitorId, query, status, page, size, sortBy, sortDir));
    }

    // Obtener resultado por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    // Registrar un resultado de carrera
    @PostMapping
    public ResponseEntity<ResultResponse> createResult(@Valid @RequestBody ResultRequest resultRequest) {
        ResultResponse resultResponse = resultService.createResult(resultRequest);
        // Si ResultResponse es Record usa .id(), si es Class con Lombok usa .getId()
        return ResponseEntity.created(URI.create("/api/v1/results/" + resultResponse.id())).body(resultResponse);
    }

    // Actualizar un resultado
    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse> updateResult(@PathVariable Long id, @Valid @RequestBody ResultRequest resultRequest) {
        return ResponseEntity.ok(resultService.updateResult(id, resultRequest));
    }

    // Actualizar estado del resultado (Ej. Oficializado, Impugnado, Anulado)
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResultResponse> updateResultStatus(
            @PathVariable Long id,
            @Valid @RequestBody ResultStatusRequest statusRequest) {
        return ResponseEntity.ok(resultService.updateResultStatus(id, statusRequest));
    }

    // Eliminar o anular un registro de resultado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}