package com.example.camelvsdwarf.race.controller;

import com.example.camelvsdwarf.race.RaceRequest;
import com.example.camelvsdwarf.race.RaceResponse;
import com.exampl.camelvsdwarf.race.RaceStatus;
import com.example.camelvsdwarf.race.RaceStatusRequest;
import com.example.camelvsdwarf.race.service.RaceService;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/races")
@RequiredArgsConstructor
public class RaceController {

    //Atributos
    private final RaceService raceService;

    //Obtener todas las carreras
    @GetMapping
    public ResponseEntity<PageResponse<RaceResponse>> getAllRaces(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) RaceStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(raceService.getAllRaces(query, status, page, size, sortBy, sortDir));
    }

    //Obtener carrera por ID
    @GetMapping("/{id}")
    public ResponseEntity<RaceResponse> getRaceById(@PathVariable Long id) {
        return ResponseEntity.ok(raceService.getRaceById(id));
    }

    //Crear carrera
    @PostMapping
    public ResponseEntity<RaceResponse> createRace(@Valid @RequestBody RaceRequest raceRequest) {
        RaceResponse raceResponse = raceService.createRace(raceRequest);
        return ResponseEntity.created(URI.create("/api/v1/races/" + raceResponse.getId())).body(raceResponse);
    }

    //Actualizar carrera
    @PutMapping("/{id}")
    public ResponseEntity<RaceResponse> updateRace(@PathVariable Long id, @Valid @RequestBody RaceRequest raceRequest) {
        return ResponseEntity.ok(raceService.updateRace(id, raceRequest));
    }

    //Actualizar estado de carrera
    @PatchMapping("/{id}/status")
    public ResponseEntity<RaceResponse> updateRaceStatus(@PathVariable Long id, @Valid @RequestBody RaceStatusRequest statusRequest) {
        return ResponseEntity.ok(raceService.updateRaceStatus(id, statusRequest));
    }

    //Eliminar carrera
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRace(@PathVariable Long id) {
        raceService.deleteRace(id);
        return ResponseEntity.noContent().build();
    }
}