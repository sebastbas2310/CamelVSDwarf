package com.example.camelvsdwarf.race.entity;

import com.example.camelvsdwarf.race.dto.RaceStatus;
import com.example.camelvsdwarf.race.dto.RaceType;
import com.example.camelvsdwarf.user.entity.AppUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "races")
@Getter
@Setter
@NoArgsConstructor
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 600)
    private String description;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Column(nullable = false, length = 150)
    private String startLocation;

    @Column(nullable = false, length = 150)
    private String finishLocation;

    @Column(nullable = false)
    private Integer distanceMeters;

    @Column(nullable = false)
    private Integer maximumParticipants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RaceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RaceStatus status = RaceStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private AppUser organizer;

    @Column(nullable = false)
    private LocalDateTime registrationDeadline;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}