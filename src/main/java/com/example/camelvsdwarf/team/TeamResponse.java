package com.example.camelvsdwarf.team;

import com.example.camelvsdwarf.team.TeamStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TeamResponse(
        Long id, String name, String description, String coach, TeamStatus status,
        int maximumMembers, LocalDateTime createdAt, int victories, int defeats,
        List<Long> competitorIds
) {
}