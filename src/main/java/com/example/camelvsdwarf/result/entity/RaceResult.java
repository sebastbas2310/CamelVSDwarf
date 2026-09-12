package com.example.camelvsdwarf.result.entity;

import com.example.camelvsdwarf.registration.RaceRegistration;
import com.example.camelvsdwarf.race.Race;
import com.example.camelvsdwarf.result.dto.ResultStatus;
import com.example.camelvsdwarf.user.entity.AppUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "race_results")
@Getter
@Setter
@NoArgsConstructor
public class RaceResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_id", nullable = false)
    private RaceRegistration registration;

    private Integer startingPosition;

    private Integer finalPosition;

    @Column(precision = 10, scale = 3)
    private BigDecimal completionTimeSeconds;

    @Column(precision = 10, scale = 3, nullable = false)
    private BigDecimal penaltyTimeSeconds = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private ResultStatus status;

    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recorded_by_id", nullable = false)
    private AppUser recordedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    void onCreate() {
        recordedAt = LocalDateTime.now();
    }
}