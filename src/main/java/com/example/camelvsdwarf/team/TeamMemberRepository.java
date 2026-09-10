package com.example.camelvsdwarf.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamIdAndCompetitorId(Long teamId, Long competitorId);
    boolean existsByCompetitorIdAndTeam_Status(Long competitorId, teamStatus status);
    long countByTeamId(Long teamId);
    List<TeamMember> findByTeamId(Long teamId);
    void deleteByTeamIdAndCompetitorId(Long teamId, Long competitorId);
}