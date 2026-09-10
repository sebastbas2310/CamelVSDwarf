package com.example.camelvsdwarf.team;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "teams", uniqueConstraints = @UniqueConstraint(name = "uk_team_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 120)
    private String coach;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private teamStatus status = teamStatus.ACTIVE;

    @Column(nullable = false)
    private int maximumMembers = 5;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int victories;

    @Column(nullable = false)
    private int defeats;

    @Column(nullable = false)
    private String responsible_person;

    @Column
    private List<Participante> participantes = new ArrayList<>();
}

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}