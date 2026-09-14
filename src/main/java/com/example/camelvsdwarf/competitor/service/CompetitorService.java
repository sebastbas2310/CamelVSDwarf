package com.example.camelvsdwarf.competitor.service;

import com.example.camelvsdwarf.competitor.*;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompetitorService {
    private final CompetitorRepository competitorRepository;

    @Transactional(readOnly = true)
    public PageResponse<CompetitorResponse> getAllCompetitors(String query, CompetitorStatus status, int page, int size, String sortBy, String sortDir) {
        var sort = Sort.by(Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Competitor> result;
        if (query == null || query.isBlank()) {
            result = status == null ? competitorRepository.findAll(pageable) : competitorRepository.findByStatus(status, pageable);
        } else {
            String trimmedQuery = query.trim();
            result = competitorRepository.findByNameContainingIgnoreCaseOrNicknameContainingIgnoreCase(trimmedQuery, trimmedQuery, pageable);
            if (status != null) {
                var filtered = result.getContent().stream()
                        .filter(competitor -> competitor.getStatus() == status)
                        .toList();
                return new PageResponse<>(
                        filtered.stream().map(this::toResponse).toList(),
                        result.getNumber(),
                        result.getSize(),
                        filtered.size(),
                        (int) Math.ceil((double) filtered.size() / result.getSize()),
                        result.isFirst(),
                        result.isLast()
                );
            }
        }

        return PageResponse.from(result.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public CompetitorResponse getCompetitorById(Long id) {
        return toResponse(getCompetitor(id));
    }

    @Transactional
    public CompetitorResponse createCompetitor(CompetitorRequest request) {
        String nickname = normalizeNickname(request.nickname());
        if (competitorRepository.existsByNicknameIgnoreCase(nickname)) {
            throw new BusinessConflictException("Competitor nickname is already in use");
        }

        Competitor competitor = new Competitor();
        apply(competitor, request);
        return toResponse(competitorRepository.save(competitor));
    }

    @Transactional
    public CompetitorResponse updateCompetitor(Long id, CompetitorRequest request) {
        Competitor competitor = getCompetitor(id);
        String nickname = normalizeNickname(request.nickname());
        if (competitorRepository.existsByNicknameIgnoreCaseAndIdNot(nickname, id)) {
            throw new BusinessConflictException("Competitor nickname is already in use");
        }

        apply(competitor, request);
        return toResponse(competitorRepository.save(competitor));
    }

    @Transactional
    public CompetitorResponse updateCompetitorStatus(Long id, CompetitorStatusRequest request) {
        Competitor competitor = getCompetitor(id);
        competitor.setStatus(request.status());
        return toResponse(competitorRepository.save(competitor));
    }

    @Transactional
    public void deleteCompetitor(Long id) {
        competitorRepository.delete(getCompetitor(id));
    }

    private Competitor getCompetitor(Long id) {
        return competitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competitor with ID " + id + " was not found"));
    }

    private String normalizeNickname(String nickname) {
        return nickname == null ? null : nickname.trim();
    }

    private void apply(Competitor competitor, CompetitorRequest request) {
        competitor.setName(request.name().trim());
        competitor.setNickname(normalizeNickname(request.nickname()));
        competitor.setType(request.type());
        competitor.setDateOfBirth(request.dateOfBirth());
        competitor.setWeight(request.weight());
        competitor.setHeight(request.height());
        competitor.setOrigin(request.origin() == null ? null : request.origin().trim());
        if (competitor.getStatus() == null) {
            competitor.setStatus(CompetitorStatus.ACTIVE);
        }
    }

    private CompetitorResponse toResponse(Competitor competitor) {
        return new CompetitorResponse(
                competitor.getId(),
                competitor.getName(),
                competitor.getNickname(),
                competitor.getType(),
                competitor.getDateOfBirth(),
                competitor.getWeight(),
                competitor.getHeight(),
                competitor.getOrigin(),
                competitor.getStatus(),
                competitor.getRegisteredAt(),
                competitor.getVictories(),
                competitor.getDefeats(),
                competitor.getCompletedRaces()
        );
    }
}
