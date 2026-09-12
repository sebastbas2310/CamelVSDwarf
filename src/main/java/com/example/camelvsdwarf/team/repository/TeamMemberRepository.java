package com.example.camelvsdwarf.team.repository;

import com.example.camelvsdwarf.team.TeamMember;
import com.example.camelvsdwarf.team.dto.TeamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamIdAndCompetitorId(Long teamId, Long competitorId);
    boolean existsByCompetitorIdAndTeam_Status(Long competitorId, TeamStatus status);
    long countByTeamId(Long teamId);
    List<TeamMember> findByTeamId(Long teamId);
}