package com.example.camelvsdwarf.race.service;

import com.example.camelvsdwarf.race.*;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import com.example.camelvsdwarf.user.AppUser;
import com.example.camelvsdwarf.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RaceService {
    private final RaceRepository raceRepository;
    private final AppUserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResponse<RaceResponse> getAllRaces(String query, RaceStatus status, int page, int size, String sortBy, String sortDir) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC), sortBy));
        var races = query == null || query.isBlank()
                ? status == null ? raceRepository.findAll(pageable) : raceRepository.findByStatus(status, pageable)
                : raceRepository.findByNameContainingIgnoreCase(query.trim(), pageable);
        return PageResponse.from(races.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public RaceResponse getRaceById(Long id) { return toResponse(getRace(id)); }

    @Transactional
    public RaceResponse createRace(RaceRequest request) {
        validateDates(request);
        AppUser organizer = userRepository.findAll().stream().findFirst().orElseThrow(() -> new BusinessConflictException("An organizer user is required"));
        Race race = new Race(); apply(race, request); race.setOrganizer(organizer); return toResponse(raceRepository.save(race));
    }

    @Transactional
    public RaceResponse updateRace(Long id, RaceRequest request) {
        Race race = getRace(id);
        if (race.getStatus() == RaceStatus.COMPLETED || race.getStatus() == RaceStatus.CANCELLED) throw new BusinessConflictException("This race cannot be edited");
        validateDates(request); apply(race, request); return toResponse(raceRepository.save(race));
    }

    @Transactional
    public RaceResponse updateRaceStatus(Long id, RaceStatusRequest request) {
        Race race = getRace(id);
        if (race.getStatus() == RaceStatus.COMPLETED && request.status() != RaceStatus.COMPLETED) throw new BusinessConflictException("A completed race cannot be reopened");
        race.setStatus(request.status()); return toResponse(raceRepository.save(race));
    }

    @Transactional
    public void deleteRace(Long id) { Race race = getRace(id); race.setStatus(RaceStatus.CANCELLED); raceRepository.save(race); }

    private Race getRace(Long id) { return raceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Race with ID " + id + " was not found")); }
    private void validateDates(RaceRequest request) { if (!request.registrationDeadline().isBefore(request.scheduledAt())) throw new BusinessConflictException("Registration deadline must be before race start"); }
    private void apply(Race race, RaceRequest request) { race.setName(request.name().trim()); race.setDescription(request.description()); race.setScheduledAt(request.scheduledAt()); race.setStartLocation(request.startLocation().trim()); race.setFinishLocation(request.finishLocation().trim()); race.setDistanceMeters(request.distanceMeters()); race.setMaximumParticipants(request.maximumParticipants()); race.setType(request.type()); race.setRegistrationDeadline(request.registrationDeadline()); }
    private RaceResponse toResponse(Race r) { return new RaceResponse(r.getId(), r.getName(), r.getDescription(), r.getScheduledAt(), r.getStartLocation(), r.getFinishLocation(), r.getDistanceMeters(), r.getMaximumParticipants(), r.getType(), r.getStatus(), r.getOrganizer().getId(), r.getRegistrationDeadline(), r.getCreatedAt(), r.getUpdatedAt()); }
}
