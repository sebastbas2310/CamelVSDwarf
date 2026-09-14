package com.example.camelvsdwarf.competitor.controller;

import com.example.camelvsdwarf.competitor.CompetitorRequest;
import com.example.camelvsdwarf.competitor.CompetitorResponse;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.competitor.CompetitorStatusRequest;
import com.example.camelvsdwarf.competitor.service.CompetitorService;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/competitors")
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
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(competitorService.getAllCompetitors(query, status, page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitorResponse> getCompetitorById(@PathVariable Long id) {
        return ResponseEntity.ok(competitorService.getCompetitorById(id));
    }

    @PostMapping
    public ResponseEntity<CompetitorResponse> createCompetitor(@Valid @RequestBody CompetitorRequest request) {
        CompetitorResponse response = competitorService.createCompetitor(request);
        return ResponseEntity.created(URI.create("/api/v1/competitors/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitorResponse> updateCompetitor(@PathVariable Long id, @Valid @RequestBody CompetitorRequest request) {
        return ResponseEntity.ok(competitorService.updateCompetitor(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CompetitorResponse> updateCompetitorStatus(@PathVariable Long id, @Valid @RequestBody CompetitorStatusRequest request) {
        return ResponseEntity.ok(competitorService.updateCompetitorStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetitor(@PathVariable Long id) {
        competitorService.deleteCompetitor(id);
        return ResponseEntity.noContent().build();
    }
}
