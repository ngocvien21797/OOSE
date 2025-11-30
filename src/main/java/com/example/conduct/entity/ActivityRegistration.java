package com.example.conduct.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_registrations")
public class ActivityRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "approved", nullable = false)
    private boolean approved = false;

    @Column(name = "registration_time", nullable = false)
    private LocalDateTime registrationTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_after_event", nullable = false, length = 20)
    private StatusAfterEvent statusAfterEvent = StatusAfterEvent.UNKNOWN;

    public enum StatusAfterEvent {
        PRESENT, ABSENT, UNKNOWN
    }

    @PrePersist
    public void prePersist() {
        if (registrationTime == null) {
            registrationTime = LocalDateTime.now();
        }
    }

    // ===== GETTER / SETTER =====

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public StatusAfterEvent getStatusAfterEvent() {
        return statusAfterEvent;
    }

    public void setStatusAfterEvent(StatusAfterEvent statusAfterEvent) {
        this.statusAfterEvent = statusAfterEvent;
    }
}
