package com.example.camelvsdwarf.team.controller;

import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.team.TeamRequest;
import com.example.camelvsdwarf.team.TeamResponse;
import com.example.camelvsdwarf.team.teamStatus;
import com.example.camelvsdwarf.team.service.teamServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.net.URI;

@RestController
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
