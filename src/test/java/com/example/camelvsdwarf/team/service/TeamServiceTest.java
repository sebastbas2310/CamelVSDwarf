package com.example.camelvsdwarf.team.service;

import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.competitor.CompetitorRepository;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import com.example.camelvsdwarf.team.*;
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
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private CompetitorRepository competitorRepository;

    @InjectMocks
    private TeamService service;

    @Test
    void createTeam_shouldPersistTeam() {
        when(teamRepository.existsByNameIgnoreCase("Atlas")).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> {
            Team team = invocation.getArgument(0);
            team.setId(10L);
            return team;
        });

        var request = new TeamRequest("Atlas", "Equipo de prueba", "Coach Luna", 6);

        var response = service.createTeam(request);

        assertAll(
                () -> assertEquals(10L, response.id()),
                () -> assertEquals("Atlas", response.name()),
                () -> assertEquals("Coach Luna", response.coach()),
                () -> assertEquals(6, response.maximumMembers())
        );
    }

    @Test
    void addCompetitorToTeam_shouldAllowWhenTeamAndCompetitorAreActive() {
        Team team = new Team();
        team.setId(2L);
        team.setStatus(TeamStatus.ACTIVE);
        team.setMaximumMembers(3);
        Competitor competitor = new Competitor();
        competitor.setId(44L);
        competitor.setStatus(CompetitorStatus.ACTIVE);

        when(teamRepository.findById(2L)).thenReturn(Optional.of(team));
        when(competitorRepository.findById(44L)).thenReturn(Optional.of(competitor));
        when(teamMemberRepository.existsByTeamIdAndCompetitorId(2L, 44L)).thenReturn(false);
        when(teamMemberRepository.countByTeamId(2L)).thenReturn(1L);
        when(teamMemberRepository.existsByCompetitorIdAndTeam_Status(44L, TeamStatus.ACTIVE)).thenReturn(false);

        service.addCompetitorToTeam(2L, 44L);

        verify(teamMemberRepository).save(any(TeamMember.class));
    }

    @Test
    void addCompetitorToTeam_shouldRejectFullTeam() {
        Team team = new Team();
        team.setId(3L);
        team.setStatus(TeamStatus.ACTIVE);
        team.setMaximumMembers(1);
        Competitor competitor = new Competitor();
        competitor.setId(12L);
        competitor.setStatus(CompetitorStatus.ACTIVE);

        when(teamRepository.findById(3L)).thenReturn(Optional.of(team));
        when(competitorRepository.findById(12L)).thenReturn(Optional.of(competitor));
        when(teamMemberRepository.existsByTeamIdAndCompetitorId(3L, 12L)).thenReturn(false);
        when(teamMemberRepository.countByTeamId(3L)).thenReturn(1L);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.addCompetitorToTeam(3L, 12L));

        assertEquals("Team capacity exceeded", exception.getMessage());
    }

    @Test
    void removeCompetitorFromTeam_shouldDeleteMembership() {
        Team team = new Team();
        team.setId(4L);
        when(teamRepository.findById(4L)).thenReturn(Optional.of(team));
        when(teamMemberRepository.existsByTeamIdAndCompetitorId(4L, 18L)).thenReturn(true);

        service.removeCompetitorFromTeam(4L, 18L);

        verify(teamMemberRepository).deleteByTeamIdAndCompetitorId(4L, 18L);
    }
}
