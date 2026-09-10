package com.example.camelvsdwarf.team.controller;

<<<<<<< HEAD
import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.team.TeamRequest;
import com.example.camelvsdwarf.team.TeamResponse;
import com.example.camelvsdwarf.team.teamStatus;
import com.example.camelvsdwarf.team.service.teamServices;
=======
import com.example.camelvsdwarf.team.TeamRequest;
import com.example.camelvsdwarf.team.TeamResponse;
import com.example.camelvsdwarf.team.TeamStatus;
import com.example.camelvsdwarf.team.TeamStatusRequest;
import com.example.camelvsdwarf.team.service.TeamService;
import com.example.camelvsdwarf.shared.dto.PageResponse;
>>>>>>> 1080afc85bbf35d4cace8047d8b4c7c9270b896d
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> 1080afc85bbf35d4cace8047d8b4c7c9270b896d

import java.net.URI;

@RestController
<<<<<<< HEAD
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final teamServices teamService;

    @GetMapping
    public ResponseEntity<PageResponse<TeamResponse>> getAllTeams(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) teamStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(teamService.findAll(query, status, page, size, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest request) {
        TeamResponse response = teamService.create(request);
        return ResponseEntity.created(URI.create("/api/teams/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable Long id, @Valid @RequestBody TeamRequest request) {
        return ResponseEntity.ok(teamService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TeamResponse> changeStatus(
            @PathVariable Long id, @RequestParam teamStatus status) {
        return ResponseEntity.ok(teamService.changeStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        teamService.delete(id);
    }

    @PostMapping("/{teamId}/members/{competitorId}")
    public ResponseEntity<TeamResponse> addMember(
            @PathVariable Long teamId, @PathVariable Long competitorId) {
        return ResponseEntity.ok(teamService.addMember(teamId, competitorId));
    }

    @DeleteMapping("/{teamId}/members/{competitorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable Long teamId, @PathVariable Long competitorId) {
        teamService.removeMember(teamId, competitorId);
    }
}
=======
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
>>>>>>> 1080afc85bbf35d4cace8047d8b4c7c9270b896d
