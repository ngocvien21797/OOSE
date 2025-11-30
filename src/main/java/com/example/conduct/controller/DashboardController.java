package com.example.conduct.controller;

import com.example.conduct.entity.Activity;
import com.example.conduct.entity.User;
import com.example.conduct.entity.User.Role;
import com.example.conduct.service.ActivityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final ActivityService activityService;

    public DashboardController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (currentUser.getRole() != Role.ADMIN) {
            // không phải admin thì đá ra login (hoặc trang khác tuỳ bạn)
            return "redirect:/login";
        }

        // info user
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("role", currentUser.getRole().name());

        // ====== DỮ LIỆU CHO 4 CARD TRÊN DASHBOARD ======
        // tạm thời cho 3 cái là 0, sau này bạn nối với StudentService, SelfScoreService...
        model.addAttribute("totalActiveStudents", 0);
        model.addAttribute("inactiveOrSuspendedStudents", 0);
        model.addAttribute("pendingSelfScores", 0);

        long openClassActivities = activityService.countOpenActivities();
        model.addAttribute("openClassActivities", openClassActivities);

        // ====== DỮ LIỆU BẢNG HOẠT ĐỘNG ĐANG MỞ / SẮP DIỄN RA ======
        List<Activity> upcomingActivities = activityService.getUpcomingActivitiesForDashboard();
        model.addAttribute("upcomingActivities", upcomingActivities);

        return "admin/dashboard-admin"; // templates/admin/dashboard-admin.html
    }

    @GetMapping("/students/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("role", currentUser.getRole().name());

        return "students/dashboard-student"; // templates/students/dashboard-student.html
    }
}
