package com.example.conduct.repository;

import com.example.conduct.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // Hoạt động OPEN, chưa hết hạn đăng ký, lấy tối đa 5 cái, sort theo ngày bắt đầu
    List<Activity> findTop5ByStatusAndRegistrationEndAfterOrderByRegistrationStartAsc(
            Activity.Status status,
            LocalDateTime now
    );

    long countByStatus(Activity.Status status);
}
