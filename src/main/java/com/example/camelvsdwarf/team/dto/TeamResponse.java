package com.example.camelvsdwarf.team;

import java.time.LocalDateTime;
import java.util.List;

public record TeamResponse(
        Long id, String name, String description, String coach, teamStatus status,
        int maximumMembers, LocalDateTime createdAt, int victories, int defeats,
        List<Long> competitorIds
) {
}