package com.example.camelvsdwarf.registration.entity;

import com.example.camelvsdwarf.competitor.Competitor;
import com.example.camelvsdwarf.race.Race;
import com.example.camelvsdwarf.registration.dto.ParticipantType;
import com.example.camelvsdwarf.registration.dto.RegistrationStatus;
import com.example.camelvsdwarf.team.Team;
import com.example.camelvsdwarf.user.entity.AppUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "race_registrations")
@Getter
@Setter
@NoArgsConstructor
public class RaceRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ParticipantType participantType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competitor_id")
    private Competitor competitor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    @Column(nullable = false)
    private Integer startingPosition;

    @Column(length = 500)
    private String validationNotes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registered_by_id", nullable = false)
    private AppUser registeredBy;

    @PrePersist
    void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}