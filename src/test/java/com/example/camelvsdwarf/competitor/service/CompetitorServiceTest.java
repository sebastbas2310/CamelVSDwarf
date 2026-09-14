package com.example.camelvsdwarf.competitor.service;

import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.competitor.CompetitorRepository;
import com.example.camelvsdwarf.competitor.CompetitorRequest;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.competitor.CompetitorStatusRequest;
import com.example.camelvsdwarf.competitor.CompetitorType;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitorServiceTest {

    @Mock
    private CompetitorRepository competitorRepository;

    @InjectMocks
    private CompetitorService service;

    @Test
    void createCompetitor_shouldPersistCompetitor() {
        var request = new CompetitorRequest(
                "Mickey",
                "Miki",
                CompetitorType.DWARF,
                LocalDate.of(1997, 1, 15),
                new BigDecimal("58.50"),
                new BigDecimal("145.00"),
                "Málaga"
        );

        when(competitorRepository.existsByNicknameIgnoreCase("Miki")).thenReturn(false);
        when(competitorRepository.save(any(Competitor.class))).thenAnswer(invocation -> {
            Competitor competitor = invocation.getArgument(0);
            competitor.setId(12L);
            return competitor;
        });

        var response = service.createCompetitor(request);

        assertAll(
                () -> assertEquals(12L, response.id()),
                () -> assertEquals("Mickey", response.name()),
                () -> assertEquals("Miki", response.nickname()),
                () -> assertEquals(CompetitorStatus.ACTIVE, response.status())
        );
        verify(competitorRepository).save(any(Competitor.class));
    }

    @Test
    void createCompetitor_shouldRejectDuplicateNickname() {
        var request = new CompetitorRequest(
                "Mickey",
                " Miki ",
                CompetitorType.DWARF,
                LocalDate.of(1997, 1, 15),
                new BigDecimal("58.50"),
                new BigDecimal("145.00"),
                "Málaga"
        );

        when(competitorRepository.existsByNicknameIgnoreCase("Miki")).thenReturn(true);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.createCompetitor(request));

        assertEquals("Competitor nickname is already in use", exception.getMessage());
        verify(competitorRepository, never()).save(any(Competitor.class));
    }

    @Test
    void updateCompetitorStatus_shouldUpdateStatus() {
        Competitor competitor = new Competitor();
        competitor.setId(5L);
        competitor.setStatus(CompetitorStatus.ACTIVE);

        when(competitorRepository.findById(5L)).thenReturn(Optional.of(competitor));
        when(competitorRepository.save(any(Competitor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.updateCompetitorStatus(5L, new CompetitorStatusRequest(CompetitorStatus.INJURED));

        assertEquals(CompetitorStatus.INJURED, response.status());
        assertEquals(CompetitorStatus.INJURED, competitor.getStatus());
        verify(competitorRepository).save(competitor);
    }
}
