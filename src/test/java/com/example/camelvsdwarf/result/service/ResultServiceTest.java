package com.example.camelvsdwarf.result.service;

import com.example.camelvsdwarf.race.Race;
import com.example.camelvsdwarf.registration.RaceRegistration;
import com.example.camelvsdwarf.registration.RaceRegistrationRepository;
import com.example.camelvsdwarf.result.*;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.user.AppUser;
import com.example.camelvsdwarf.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {

    @Mock
    private RaceResultRepository repository;

    @Mock
    private RaceRegistrationRepository registrationRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private ResultService service;

    @Test
    void createResult_shouldPersistResultForRegistration() {
        Race race = new Race();
        race.setId(17L);
        RaceRegistration registration = new RaceRegistration();
        registration.setId(9L);
        registration.setRace(race);
        AppUser recordedBy = new AppUser();
        recordedBy.setId(42L);

        when(registrationRepository.findById(9L)).thenReturn(Optional.of(registration));
        when(repository.existsByRaceIdAndRegistrationId(17L, 9L)).thenReturn(false);
        when(userRepository.findAll()).thenReturn(List.of(recordedBy));
        when(repository.save(any(RaceResult.class))).thenAnswer(invocation -> {
            RaceResult result = invocation.getArgument(0);
            result.setId(55L);
            return result;
        });

        var request = new ResultRequest(9L, 1, 2, new BigDecimal("14.250"), BigDecimal.ZERO, ResultStatus.FINISHED, "Todo correcto");

        var response = service.createResult(request);

        assertAll(
                () -> assertEquals(55L, response.id()),
                () -> assertEquals(17L, response.raceId()),
                () -> assertEquals(9L, response.registrationId()),
                () -> assertEquals(ResultStatus.FINISHED, response.status()),
                () -> assertEquals(42L, response.recordedById())
        );
    }

    @Test
    void createResult_shouldRejectDuplicateRegistration() {
        Race race = new Race();
        race.setId(7L);
        RaceRegistration registration = new RaceRegistration();
        registration.setId(3L);
        registration.setRace(race);

        when(registrationRepository.findById(3L)).thenReturn(Optional.of(registration));
        when(repository.existsByRaceIdAndRegistrationId(7L, 3L)).thenReturn(true);

        var request = new ResultRequest(3L, 1, 2, new BigDecimal("10.0"), BigDecimal.ZERO, ResultStatus.FINISHED, null);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.createResult(request));

        assertEquals("A result already exists for this registration", exception.getMessage());
    }

    @Test
    void updateResultStatus_shouldPersistStatusChange() {
        RaceResult result = new RaceResult();
        result.setId(4L);
        result.setRace(new Race());
        result.getRace().setId(13L);
        result.setRegistration(new RaceRegistration());
        result.getRegistration().setId(21L);
        result.setRecordedBy(new AppUser());
        result.getRecordedBy().setId(5L);
        when(repository.findById(4L)).thenReturn(Optional.of(result));
        when(repository.save(any(RaceResult.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.updateResultStatus(4L, new ResultStatusRequest(ResultStatus.DID_NOT_FINISH));

        assertEquals(ResultStatus.DID_NOT_FINISH, response.status());
        verify(repository).save(result);
    }
}
