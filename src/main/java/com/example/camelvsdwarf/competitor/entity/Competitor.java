package com.example.camelvsdwarf.competitor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "competitors", uniqueConstraints = @UniqueConstraint(name = "uk_competitor_nickname", columnNames = "nickname"))
@Getter
@Setter
@NoArgsConstructor
public class Competitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 80, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CompetitorType type;

    private LocalDate dateOfBirth;

    @NotNull
    @Positive
    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal weight;

    @Positive
    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal height;

    @Column(length = 120)
    private String origin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CompetitorStatus status = CompetitorStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @Column(nullable = false)
    private int victories;

    @Column(nullable = false)
    private int defeats;

    @Column(nullable = false)
    private int completedRaces;

    @Column(nullable = true)
    private String Team;

    @PrePersist
    void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}