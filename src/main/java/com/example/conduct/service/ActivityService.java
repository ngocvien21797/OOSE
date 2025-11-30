package com.example.conduct.service;

import com.example.conduct.entity.Activity;
import com.example.conduct.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    public Activity getById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public void delete(Long id) {
        activityRepository.deleteById(id);
    }

    // ================= DASHBOARD =================

    /**
     * Lấy một số hoạt động đang mở / sắp diễn ra cho dashboard.
     * Ở đây lọc: status = OPEN và registration_end > now
     */
    public List<Activity> getUpcomingActivitiesForDashboard() {
        LocalDateTime now = LocalDateTime.now();
        return activityRepository
                .findTop5ByStatusAndRegistrationEndAfterOrderByRegistrationStartAsc(
                        Activity.Status.OPEN,
                        now
                );
    }

    /**
     * Đếm số hoạt động đang ở trạng thái OPEN (để show trên card)
     */
    public long countOpenActivities() {
        return activityRepository.countByStatus(Activity.Status.OPEN);
    }
}
