package com.example.camelvsdwarf.team.controller;

import com.example.camelvsdwarf.team.TeamRequest;
import com.example.camelvsdwarf.team.TeamResponse;
import com.example.camelvsdwarf.team.TeamStatus;
import com.example.camelvsdwarf.team.TeamStatusRequest;
import com.example.camelvsdwarf.team.service.TeamService;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    //Atributos
    private final TeamService teamService;

    //Obtener todos los equipos
    @GetMapping
    public ResponseEntity<PageResponse<TeamResponse>> getAllTeams(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) TeamStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(teamService.getAllTeams(query, status, page, size, sortBy, sortDir));

    }

    //Obtener equipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    //Crear equipo
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest teamRequest) {
        TeamResponse teamResponse = teamService.createTeam(teamRequest);
        return ResponseEntity.created(URI.create("/api/v1/teams/" + teamResponse.getId())).body(teamResponse);
    }

    //Actualizar equipo
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequest teamRequest) {
        return ResponseEntity.ok(teamService.updateTeam(id, teamRequest));
    }

    //Actualizar estado del equipo
    @PatchMapping("/{id}/status")
    public ResponseEntity<TeamResponse> updateTeamStatus(@PathVariable Long id, @Valid @RequestBody TeamStatusRequest statusRequest) {
        return ResponseEntity.ok(teamService.updateTeamStatus(id, statusRequest));
    }

    //Eliminar equipo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    //Agregar un competidor a un equipo
    @PostMapping("/{teamId}/competitors/{competitorId}")
    public ResponseEntity<Void> addCompetitorToTeam(@PathVariable Long teamId, @PathVariable Long competitorId) {
        teamService.addCompetitorToTeam(teamId, competitorId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Eliminar un competidor de un equipo
    @DeleteMapping("/{teamId}/competitors/{competitorId}")
    public ResponseEntity<Void> removeCompetitorFromTeam(@PathVariable Long teamId, @PathVariable Long competitorId) {
        teamService.removeCompetitorFromTeam(teamId, competitorId);
        return ResponseEntity.noContent().build();
    }
}