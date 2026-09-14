package com.example.camelvsdwarf.race.service;

import com.example.camelvsdwarf.race.*;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.user.AppUser;
import com.example.camelvsdwarf.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private RaceService service;

    @Test
    void createRace_shouldCreateRaceAndAssignOrganizer() {
        AppUser organizer = new AppUser();
        organizer.setId(7L);
        when(userRepository.findAll()).thenReturn(List.of(organizer));
        when(raceRepository.save(any(Race.class))).thenAnswer(invocation -> {
            Race race = invocation.getArgument(0);
            race.setId(11L);
            return race;
        });

        var request = new RaceRequest(
                "Gran Carrera",
                "Carrera de prueba",
                LocalDateTime.now().plusDays(10),
                "Málaga",
                "Valencia",
                4200,
                12,
                RaceType.INDIVIDUAL,
                LocalDateTime.now().plusDays(4)
        );

        var response = service.createRace(request);

        assertAll(
                () -> assertEquals(11L, response.id()),
                () -> assertEquals("Gran Carrera", response.name()),
                () -> assertEquals(7L, response.organizerId()),
                () -> assertEquals(RaceStatus.DRAFT, response.status())
        );
        verify(raceRepository).save(any(Race.class));
    }

    @Test
    void createRace_shouldRejectDatesWhenRegistrationDeadlineIsNotBeforeStart() {
        var request = new RaceRequest(
                "Carrera inválida",
                "desc",
                LocalDateTime.now().plusDays(2),
                "A",
                "B",
                5000,
                10,
                RaceType.MIXED,
                LocalDateTime.now().plusDays(2)
        );

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.createRace(request));

        assertEquals("Registration deadline must be before race start", exception.getMessage());
        verify(raceRepository, never()).save(any(Race.class));
    }

    @Test
    void updateRaceStatus_shouldRejectReopeningCompletedRace() {
        Race completedRace = new Race();
        completedRace.setId(5L);
        completedRace.setStatus(RaceStatus.COMPLETED);
        when(raceRepository.findById(5L)).thenReturn(Optional.of(completedRace));

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.updateRaceStatus(5L, new RaceStatusRequest(RaceStatus.OPEN_FOR_REGISTRATION)));

        assertEquals("A completed race cannot be reopened", exception.getMessage());
    }

    @Test
    void deleteRace_shouldMarkRaceAsCancelled() {
        Race race = new Race();
        race.setId(8L);
        race.setStatus(RaceStatus.OPEN_FOR_REGISTRATION);
        when(raceRepository.findById(8L)).thenReturn(Optional.of(race));
        when(raceRepository.save(any(Race.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.deleteRace(8L);

        assertEquals(RaceStatus.CANCELLED, race.getStatus());
        verify(raceRepository).save(race);
    }
}
