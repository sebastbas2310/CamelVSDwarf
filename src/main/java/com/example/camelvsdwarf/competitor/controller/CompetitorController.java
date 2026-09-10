package com.example.camelvsdwarf.competitor.controller;

import com.example.camelvsdwarf.competitor.CompetitorRequest;
import com.example.camelvsdwarf.competitor.CompetitorResponse;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.competitor.CompetitorStatusRequest;
import com.example.camelvsdwarf.competitor.service.CompetitorService;
import com.example.camelvsdwarf.shared.dto.PageResponse;
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
@RequestMapping("/api/competitors")
@RequiredArgsConstructor
public class CompetitorController {

    private final CompetitorService competitorService;

    @GetMapping
    public ResponseEntity<PageResponse<CompetitorResponse>> getAllCompetitors(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) CompetitorStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(competitorService.findAll(query, status, page, size, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitorResponse> getCompetitor(@PathVariable Long id) {
        return ResponseEntity.ok(competitorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CompetitorResponse> createCompetitor(@Valid @RequestBody CompetitorRequest request) {
        CompetitorResponse response = competitorService.create(request);
        return ResponseEntity.created(URI.create("/api/competitors/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitorResponse> updateCompetitor(
            @PathVariable Long id, @Valid @RequestBody CompetitorRequest request) {
        return ResponseEntity.ok(competitorService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CompetitorResponse> changeStatus(
            @PathVariable Long id, @Valid @RequestBody CompetitorStatusRequest request) {
        return ResponseEntity.ok(competitorService.changeStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompetitor(@PathVariable Long id) {
        competitorService.delete(id);
    }
}
