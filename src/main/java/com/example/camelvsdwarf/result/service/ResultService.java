package com.example.camelvsdwarf.result.service;

import com.example.camelvsdwarf.registration.RaceRegistrationRepository;
import com.example.camelvsdwarf.result.*;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import com.example.camelvsdwarf.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final RaceResultRepository repository;
    private final RaceRegistrationRepository registrationRepository;
    private final AppUserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResponse<ResultResponse> getAllResults(Long raceId, Long competitorId, String query, ResultStatus status, int page, int size, String sortBy, String sortDir) {
        var values = repository.findAll(PageRequest.of(page, size)).map(this::toResponse);
        return PageResponse.from(values);
    }
    @Transactional(readOnly = true) public ResultResponse getResultById(Long id) { return toResponse(get(id)); }
    @Transactional public ResultResponse createResult(ResultRequest request) {
        var registration = registrationRepository.findById(request.registrationId()).orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        if (repository.existsByRaceIdAndRegistrationId(registration.getRace().getId(), registration.getId())) throw new BusinessConflictException("A result already exists for this registration");
        RaceResult result = new RaceResult(); result.setRace(registration.getRace()); result.setRegistration(registration); apply(result, request); result.setRecordedBy(userRepository.findAll().stream().findFirst().orElseThrow(() -> new BusinessConflictException("A user is required"))); return toResponse(repository.save(result));
    }
    @Transactional public ResultResponse updateResult(Long id, ResultRequest request) { RaceResult result = get(id); apply(result, request); return toResponse(repository.save(result)); }
    @Transactional public ResultResponse updateResultStatus(Long id, ResultStatusRequest request) { RaceResult result = get(id); result.setStatus(request.status()); return toResponse(repository.save(result)); }
    @Transactional public void deleteResult(Long id) { repository.delete(get(id)); }
    private RaceResult get(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Result with ID " + id + " was not found")); }
    private void apply(RaceResult r, ResultRequest q) { r.setStartingPosition(q.startingPosition()); r.setFinalPosition(q.finalPosition()); r.setCompletionTimeSeconds(q.completionTimeSeconds()); r.setPenaltyTimeSeconds(q.penaltyTimeSeconds() == null ? java.math.BigDecimal.ZERO : q.penaltyTimeSeconds()); r.setStatus(q.status()); r.setNotes(q.notes()); }
    private ResultResponse toResponse(RaceResult r) { return new ResultResponse(r.getId(), r.getRace().getId(), r.getRegistration().getId(), r.getStartingPosition(), r.getFinalPosition(), r.getCompletionTimeSeconds(), r.getPenaltyTimeSeconds(), r.getStatus(), r.getNotes(), r.getRecordedBy().getId(), r.getRecordedAt()); }
}
