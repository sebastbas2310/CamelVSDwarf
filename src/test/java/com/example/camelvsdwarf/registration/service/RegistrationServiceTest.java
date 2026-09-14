package com.example.camelvsdwarf.registration.service;

import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.competitor.CompetitorRepository;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.race.Race;
import com.example.camelvsdwarf.race.RaceRepository;
import com.example.camelvsdwarf.registration.*;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.team.Team;
import com.example.camelvsdwarf.team.TeamRepository;
import com.example.camelvsdwarf.user.AppUser;
import com.example.camelvsdwarf.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RaceRegistrationRepository repository;

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private CompetitorRepository competitorRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private RegistrationService service;

    @Test
    void createRegistration_shouldSaveCompetitorRegistration() {
        Race race = new Race();
        race.setId(1L);
        AppUser registrar = new AppUser();
        registrar.setId(9L);
        Competitor competitor = new Competitor();
        competitor.setId(4L);

        when(raceRepository.findById(1L)).thenReturn(Optional.of(race));
        when(userRepository.findAll()).thenReturn(List.of(registrar));
        when(competitorRepository.findById(4L)).thenReturn(Optional.of(competitor));
        when(repository.save(any(RaceRegistration.class))).thenAnswer(invocation -> {
            RaceRegistration registration = invocation.getArgument(0);
            registration.setId(25L);
            return registration;
        });

        var request = new RegistrationRequest(1L, ParticipantType.COMPETITOR, 4L, null, 3);

        var response = service.createRegistration(request);

        assertAll(
                () -> assertEquals(25L, response.id()),
                () -> assertEquals(1L, response.raceId()),
                () -> assertEquals(ParticipantType.COMPETITOR, response.participantType()),
                () -> assertEquals(4L, response.competitorId()),
                () -> assertEquals(RegistrationStatus.PENDING, response.status())
        );
    }

    @Test
    void createRegistration_shouldRejectWhenNoParticipantIsProvided() {
        var request = new RegistrationRequest(1L, ParticipantType.COMPETITOR, null, null, 1);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.createRegistration(request));

        assertEquals("Exactly one participant is required", exception.getMessage());
        verify(repository, never()).save(any(RaceRegistration.class));
    }

    @Test
    void evaluateRegistration_shouldApproveAndAddReason() {
        RaceRegistration registration = new RaceRegistration();
        registration.setId(8L);
        registration.setStatus(RegistrationStatus.PENDING);
        Race race = new Race();
        race.setId(44L);
        registration.setRace(race);
        AppUser registrar = new AppUser();
        registrar.setId(99L);
        registration.setRegisteredBy(registrar);
        when(repository.findById(8L)).thenReturn(Optional.of(registration));
        when(repository.save(any(RaceRegistration.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.evaluateRegistration(8L, new RegistrationDecisionRequest("Cumple requisitos"));

        assertAll(
                () -> assertEquals(RegistrationStatus.APPROVED, response.status()),
                () -> assertEquals("Cumple requisitos", response.validationNotes())
        );
    }

    @Test
    void updateRegistrationStatus_shouldPersistNewStatus() {
        RaceRegistration registration = new RaceRegistration();
        registration.setId(12L);
        registration.setStatus(RegistrationStatus.PENDING);
        Race race = new Race();
        race.setId(20L);
        registration.setRace(race);
        AppUser registrar = new AppUser();
        registrar.setId(77L);
        registration.setRegisteredBy(registrar);
        when(repository.findById(12L)).thenReturn(Optional.of(registration));
        when(repository.save(any(RaceRegistration.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.updateRegistrationStatus(12L, RegistrationStatus.CANCELLED);

        assertEquals(RegistrationStatus.CANCELLED, response.status());
        verify(repository).save(registration);
    }
}
