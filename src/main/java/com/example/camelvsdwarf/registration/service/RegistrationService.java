package com.example.camelvsdwarf.registration.service;

import com.example.camelvsdwarf.race.RaceRepository;
import com.example.camelvsdwarf.competitor.CompetitorRepository;
import com.example.camelvsdwarf.team.TeamRepository;
import com.example.camelvsdwarf.registration.*;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import com.example.camelvsdwarf.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RaceRegistrationRepository repository;
    private final RaceRepository raceRepository;
    private final AppUserRepository userRepository;
    private final CompetitorRepository competitorRepository;
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public PageResponse<RegistrationResponse> getAllRegistrations(String query, RegistrationStatus status, int page, int size, String sortBy, String sortDir) {
        List<RaceRegistration> values = repository.findAll(PageRequest.of(page, size)).getContent();
        if (status != null) values = values.stream().filter(r -> r.getStatus() == status).toList();
        return PageResponse.from(new PageImpl<>(values.stream().map(this::toResponse).toList(), PageRequest.of(page, size), values.size()));
    }

    @Transactional(readOnly = true)
    public RegistrationResponse getRegistrationById(Long id) { return toResponse(get(id)); }

    @Transactional
    public RegistrationResponse createRegistration(RegistrationRequest request) {
        if ((request.competitorId() == null) == (request.teamId() == null)) throw new BusinessConflictException("Exactly one participant is required");
        RaceRegistration registration = new RaceRegistration();
        registration.setRace(raceRepository.findById(request.raceId()).orElseThrow(() -> new ResourceNotFoundException("Race not found")));
        registration.setParticipantType(request.participantType()); registration.setStartingPosition(request.startingPosition()); registration.setStatus(RegistrationStatus.PENDING);
        registration.setRegisteredBy(userRepository.findAll().stream().findFirst().orElseThrow(() -> new BusinessConflictException("A user is required")));
        if (request.competitorId() != null) {
            registration.setCompetitor(competitorRepository.findById(request.competitorId())
                .orElseThrow(() -> new ResourceNotFoundException("Competitor not found")));
        }
        if (request.teamId() != null) {
            registration.setTeam(teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found")));
        }
        return toResponse(repository.save(registration));
    }

    @Transactional public RegistrationResponse updateRegistration(Long id, RegistrationRequest request) { RaceRegistration r = get(id); r.setParticipantType(request.participantType()); r.setStartingPosition(request.startingPosition()); return toResponse(repository.save(r)); }
    @Transactional public RegistrationResponse updateRegistrationStatus(Long id, RegistrationStatus status) { RaceRegistration r = get(id); r.setStatus(status); return toResponse(repository.save(r)); }
    @Transactional public RegistrationResponse evaluateRegistration(Long id, RegistrationDecisionRequest request) { RaceRegistration r = get(id); r.setStatus(RegistrationStatus.APPROVED); r.setValidationNotes(request.reason()); return toResponse(repository.save(r)); }
    @Transactional public void deleteRegistration(Long id) { repository.delete(get(id)); }
    private RaceRegistration get(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Registration with ID " + id + " was not found")); }
    private RegistrationResponse toResponse(RaceRegistration r) { return new RegistrationResponse(r.getId(), r.getRace().getId(), r.getParticipantType(), r.getCompetitor() == null ? null : r.getCompetitor().getId(), r.getTeam() == null ? null : r.getTeam().getId(), r.getRegisteredAt(), r.getStatus(), r.getStartingPosition(), r.getValidationNotes(), r.getRegisteredBy().getId()); }
}
