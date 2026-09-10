package com.example.camelvsdwarf.competitor.service;


import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.competitor.CompetitorRepository;
import com.example.camelvsdwarf.competitor.CompetitorRequest;
import com.example.camelvsdwarf.competitor.CompetitorResponse;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.competitor.CompetitorStatusRequest;
import com.example.camelvsdwarf.competitor.mapper.CompetitorMapper;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompetitorService {

    private final CompetitorRepository competitorRepository;

    //=================Read===================

    @Transactional(readOnly = true)
    public PageResponse<CompetitorResponse> findAll(String query, CompetitorStatus status, int page, int size,
                                                    String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        String normalizedQuery = query == null || query.isBlank() ? null : query.trim();
        return PageResponse.from(competitorRepository.findByFilters(normalizedQuery, status, pageable)
                .map(CompetitorMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public CompetitorResponse findById(Long id) {
        return CompetitorMapper.toResponse(getCompetitor(id));
    }

    @Transactional
    public CompetitorResponse create(CompetitorRequest request) {
        ensureNicknameAvailable(request.nickname(), null);
        return CompetitorMapper.toResponse(competitorRepository.save(CompetitorMapper.toEntity(request)));
    }

    @Transactional
    public CompetitorResponse update(Long id, CompetitorRequest request) {
        Competitor competitor = getCompetitor(id);
        if (competitor.getStatus() == CompetitorStatus.RETIRED) {
            throw new BusinessConflictException("A retired competitor cannot be edited");
        }
        ensureNicknameAvailable(request.nickname(), id);
        CompetitorMapper.updateEntity(competitor, request);
        return CompetitorMapper.toResponse(competitorRepository.save(competitor));
    }

    @Transactional
    public CompetitorResponse changeStatus(Long id, CompetitorStatusRequest request) {
        Competitor competitor = getCompetitor(id);
        competitor.setStatus(request.status());
        return CompetitorMapper.toResponse(competitorRepository.save(competitor));
    }

    @Transactional
    public void delete(Long id) {
        Competitor competitor = getCompetitor(id);
        if (competitor.getCompletedRaces() > 0) {
            competitor.setStatus(CompetitorStatus.RETIRED);
            competitorRepository.save(competitor);
            return;
        }
        competitorRepository.delete(competitor);
    }

    private Competitor getCompetitor(Long id) {
        return competitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competitor with ID " + id + " was not found"));
    }

    private void ensureNicknameAvailable(String nickname, Long id) {
        boolean exists = id == null ? competitorRepository.existsByNicknameIgnoreCase(nickname)
                : competitorRepository.existsByNicknameIgnoreCaseAndIdNot(nickname, id);
        if (exists) {
            throw new BusinessConflictException("Nickname '" + nickname + "' is already in use");
        }
    }
}
