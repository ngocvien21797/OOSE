package com.example.conduct.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Level level;     // CLASS/FACULTY/UNIVERSITY/PROVINCE

    public enum Level {
        CLASS, FACULTY, UNIVERSITY, PROVINCE
    }

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "registration_start")
    private LocalDateTime registrationStart;

    @Column(name = "registration_end")
    private LocalDateTime registrationEnd;

    @Column(name = "submission_start")
    private LocalDateTime submissionStart;

    @Column(name = "submission_end")
    private LocalDateTime submissionEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;   // DRAFT/OPEN/CLOSED/DONE/CANCELLED

    public enum Status {
        DRAFT, OPEN, CLOSED, DONE, CANCELLED
    }

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Quan hệ ngược
    @OneToMany(mappedBy = "activity")
    private List<ActivityRegistration> registrations;

    @OneToMany(mappedBy = "activity")
    private List<ActivityAttendance> attendances;

    @OneToMany(mappedBy = "activity")
    private List<ActivityEvidence> evidences;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ===== GETTER / SETTER =====

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public ClassEntity getClassEntity() {
        return classEntity;
    }

    public void setClassEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public LocalDateTime getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(LocalDateTime registrationStart) {
        this.registrationStart = registrationStart;
    }

    public LocalDateTime getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(LocalDateTime registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public LocalDateTime getSubmissionStart() {
        return submissionStart;
    }

    public void setSubmissionStart(LocalDateTime submissionStart) {
        this.submissionStart = submissionStart;
    }

    public LocalDateTime getSubmissionEnd() {
        return submissionEnd;
    }

    public void setSubmissionEnd(LocalDateTime submissionEnd) {
        this.submissionEnd = submissionEnd;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ActivityRegistration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<ActivityRegistration> registrations) {
        this.registrations = registrations;
    }

    public List<ActivityAttendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<ActivityAttendance> attendances) {
        this.attendances = attendances;
    }

    public List<ActivityEvidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<ActivityEvidence> evidences) {
        this.evidences = evidences;
    }
}
