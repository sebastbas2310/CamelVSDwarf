package com.example.camelvsdwarf.audit.entity;

import com.example.camelvsdwarf.user.entity.AppUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false, length = 80)
    private String action;

    @Column(nullable = false, length = 80)
    private String entityType;

    @Column(nullable = false, length = 80)
    private String entityId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String previousValues;

    @Column(columnDefinition = "TEXT")
    private String newValues;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}