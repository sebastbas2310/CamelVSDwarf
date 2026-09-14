package com.example.camelvsdwarf.team.service;

import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.competitor.CompetitorRepository;
import com.example.camelvsdwarf.competitor.CompetitorStatus;
import com.example.camelvsdwarf.shared.dto.PageResponse;
import com.example.camelvsdwarf.shared.exception.BusinessConflictException;
import com.example.camelvsdwarf.shared.exception.ResourceNotFoundException;
import com.example.camelvsdwarf.team.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final CompetitorRepository competitorRepository;

    @Transactional(readOnly = true)
    public PageResponse<TeamResponse> getAllTeams(String query, TeamStatus status, int page, int size, String sortBy, String sortDir) {
        var sort = Sort.by(Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC), sortBy);
        var teams = query == null || query.isBlank()
                ? status == null ? teamRepository.findAll(PageRequest.of(page, size, sort)) : teamRepository.findByStatus(status, PageRequest.of(page, size, sort))
                : teamRepository.findByNameContainingIgnoreCase(query.trim(), PageRequest.of(page, size, sort));
        return PageResponse.from(teams.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long id) { return toResponse(getTeam(id)); }

    @Transactional
    public TeamResponse createTeam(TeamRequest request) {
        if (teamRepository.existsByNameIgnoreCase(request.name())) throw new BusinessConflictException("Team name is already in use");
        Team team = new Team();
        apply(team, request);
        return toResponse(teamRepository.save(team));
    }

    @Transactional
    public TeamResponse updateTeam(Long id, TeamRequest request) {
        Team team = getTeam(id);
        if (teamRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) throw new BusinessConflictException("Team name is already in use");
        apply(team, request);
        return toResponse(teamRepository.save(team));
    }

    @Transactional
    public TeamResponse updateTeamStatus(Long id, TeamStatusRequest request) {
        Team team = getTeam(id);
        team.setStatus(request.status());
        return toResponse(teamRepository.save(team));
    }

    @Transactional
    public void deleteTeam(Long id) { teamRepository.delete(getTeam(id)); }

    @Transactional
    public void addCompetitorToTeam(Long teamId, Long competitorId) {
        Team team = getTeam(teamId);
        Competitor competitor = competitorRepository.findById(competitorId).orElseThrow(() -> new ResourceNotFoundException("Competitor not found"));
        if (team.getStatus() != TeamStatus.ACTIVE || competitor.getStatus() != CompetitorStatus.ACTIVE) throw new BusinessConflictException("Team and competitor must be active");
        if (teamMemberRepository.existsByTeamIdAndCompetitorId(teamId, competitorId)) throw new BusinessConflictException("Competitor is already in this team");
        if (teamMemberRepository.countByTeamId(teamId) >= Team.MAXIMUM_MEMBERS) throw new BusinessConflictException("Team capacity exceeded");
        if (teamMemberRepository.existsByCompetitorIdAndTeam_Status(competitorId, TeamStatus.ACTIVE)) throw new BusinessConflictException("Competitor already belongs to an active team");
        TeamMember member = new TeamMember(); member.setTeam(team); member.setCompetitor(competitor); teamMemberRepository.save(member);
    }

    @Transactional
    public void removeCompetitorFromTeam(Long teamId, Long competitorId) {
        getTeam(teamId);
        if (!teamMemberRepository.existsByTeamIdAndCompetitorId(teamId, competitorId)) throw new ResourceNotFoundException("Competitor is not in this team");
        teamMemberRepository.deleteByTeamIdAndCompetitorId(teamId, competitorId);
    }

    private Team getTeam(Long id) { return teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team with ID " + id + " was not found")); }
    private void apply(Team team, TeamRequest request) { team.setName(request.name().trim()); team.setDescription(request.description()); team.setCoach(request.coach().trim()); team.setMaximumMembers(Team.MAXIMUM_MEMBERS); }
    private TeamResponse toResponse(Team team) { return new TeamResponse(team.getId(), team.getName(), team.getDescription(), team.getCoach(), team.getStatus(), Team.MAXIMUM_MEMBERS, team.getCreatedAt(), team.getVictories(), team.getDefeats(), teamMemberRepository.findByTeamId(team.getId()).stream().map(m -> m.getCompetitor().getId()).toList()); }
}
