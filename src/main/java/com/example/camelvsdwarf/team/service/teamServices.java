package com.example.camelvsdwarf.team.service;

import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.competitor.CompetitorRepository;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import com.example.camelvsdwarf.team.Team;
import com.example.camelvsdwarf.team.TeamMember;
import com.example.camelvsdwarf.team.TeamMemberRepository;
import com.example.camelvsdwarf.team.TeamRepository;
import com.example.camelvsdwarf.team.TeamRequest;
import com.example.camelvsdwarf.team.TeamResponse;
import com.example.camelvsdwarf.team.teamStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class teamServices {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final CompetitorRepository competitorRepository;

    @Transactional(readOnly = true)
    public PageResponse<TeamResponse> findAll(String query, teamStatus status, int page, int size,
                                              String sortBy, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        String normalizedQuery = query == null || query.isBlank() ? null : query.trim();
        return PageResponse.from(teamRepository.findByFilters(normalizedQuery, status, pageable)
                .map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public TeamResponse findById(Long id) {
        return toResponse(getTeam(id));
    }

    @Transactional
    public TeamResponse create(TeamRequest request) {
        ensureNameAvailable(request.name(), null);
        Team team = new Team();
        updateFields(team, request);
        return toResponse(teamRepository.save(team));
    }

    @Transactional
    public TeamResponse update(Long id, TeamRequest request) {
        Team team = getTeam(id);
        if (team.getStatus() == teamStatus.INACTIVE) {
            throw new BusinessConflictException("An inactive team cannot be edited");
        }
        ensureNameAvailable(request.name(), id);
        updateFields(team, request);
        return toResponse(teamRepository.save(team));
    }

    @Transactional
    public TeamResponse changeStatus(Long id, teamStatus status) {
        Team team = getTeam(id);
        team.setStatus(status);
        return toResponse(teamRepository.save(team));
    }

    @Transactional
    public void delete(Long id) {
        Team team = getTeam(id);
        if (team.getVictories() > 0 || team.getDefeats() > 0) {
            team.setStatus(teamStatus.INACTIVE);
            teamRepository.save(team);
            return;
        }
        teamRepository.delete(team);
    }

    @Transactional
    public TeamResponse addMember(Long teamId, Long competitorId) {
        Team team = getTeam(teamId);
        Competitor competitor = getCompetitor(competitorId);
        if (team.getStatus() != teamStatus.ACTIVE) {
            throw new BusinessConflictException("Only active teams can add competitors");
        }
        if (competitor.getStatus() != CompetitorStatus.ACTIVE) {
            throw new BusinessConflictException("Only active competitors can join a team");
        }
        if (teamMemberRepository.existsByTeamIdAndCompetitorId(teamId, competitorId)) {
            throw new BusinessConflictException("The competitor is already a member of this team");
        }
        if (teamMemberRepository.countByTeamId(teamId) >= team.getMaximumMembers()) {
            throw new BusinessConflictException("The team has reached its maximum number of members");
        }
        if (teamMemberRepository.existsByCompetitorIdAndTeam_Status(competitorId, teamStatus.ACTIVE)) {
            throw new BusinessConflictException("A competitor cannot belong to two active teams");
        }
        TeamMember member = new TeamMember();
        member.setTeam(team);
        member.setCompetitor(competitor);
        teamMemberRepository.save(member);
        return toResponse(team);
    }

    @Transactional
    public void removeMember(Long teamId, Long competitorId) {
        getTeam(teamId);
        if (!teamMemberRepository.existsByTeamIdAndCompetitorId(teamId, competitorId)) {
            throw new ResourceNotFoundException("Competitor is not a member of this team");
        }
        teamMemberRepository.deleteByTeamIdAndCompetitorId(teamId, competitorId);
    }

    private Team getTeam(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team with ID " + id + " was not found"));
    }

    private Competitor getCompetitor(Long id) {
        return competitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competitor with ID " + id + " was not found"));
    }

    private void ensureNameAvailable(String name, Long id) {
        boolean exists = id == null ? teamRepository.existsByNameIgnoreCase(name)
                : teamRepository.existsByNameIgnoreCaseAndIdNot(name, id);
        if (exists) {
            throw new BusinessConflictException("Team name '" + name + "' is already in use");
        }
    }

    private void updateFields(Team team, TeamRequest request) {
        team.setName(request.name().trim());
        team.setDescription(request.description() == null ? null : request.description().trim());
        team.setCoach(request.coach().trim());
        team.setMaximumMembers(request.maximumMembers());
    }

    private TeamResponse toResponse(Team team) {
        return new TeamResponse(team.getId(), team.getName(), team.getDescription(), team.getCoach(),
                team.getStatus(), team.getMaximumMembers(), team.getCreatedAt(), team.getVictories(),
                team.getDefeats(), teamMemberRepository.findByTeamId(team.getId()).stream()
                        .map(member -> member.getCompetitor().getId()).toList());
    }
}
