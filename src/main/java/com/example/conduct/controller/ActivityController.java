package com.example.conduct.controller;

import com.example.conduct.entity.Activity;
import com.example.conduct.entity.ClassEntity;
import com.example.conduct.entity.User;
import com.example.conduct.service.ActivityService;
import com.example.conduct.service.ClassService;
import com.example.conduct.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final ClassService classService;
    private final AuthService authService; // nếu ông có, dùng để lấy currentUser

    public ActivityController(ActivityService activityService,
                              ClassService classService,
                              AuthService authService) {
        this.activityService = activityService;
        this.classService = classService;
        this.authService = authService;
    }

    @GetMapping
    public String listActivities(Model model) {
        List<Activity> activities = activityService.getAll();
        List<ClassEntity> classes = classService.getAllClasses();

        model.addAttribute("activities", activities);
        model.addAttribute("classes", classes);
        model.addAttribute("activityForm", new Activity());

        // view ông tự tạo: templates/admin/admin-activities.html
        return "admin/admin-activities";
    }

    @PostMapping("/save")
    public String saveActivity(@ModelAttribute("activityForm") Activity activity,
                               @RequestParam("classId") Long classId,
                               HttpSession session) {

        ClassEntity clazz = classService.getById(classId);
        activity.setClassEntity(clazz);

        // current user (admin / lớp trưởng)
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            activity.setCreatedBy(currentUser);
        }

        if (activity.getStatus() == null) {
            activity.setStatus(Activity.Status.DRAFT);
        }

        activityService.save(activity);
        return "redirect:/admin/activities";
    }

    @GetMapping("/edit/{id}")
    public String editActivity(@PathVariable Long id, Model model) {
        Activity activity = activityService.getById(id);
        List<ClassEntity> classes = classService.getAllClasses();

        model.addAttribute("activityForm", activity);
        model.addAttribute("classes", classes);
        model.addAttribute("activities", activityService.getAll());

        return "admin/admin-activities";
    }

    @PostMapping("/delete/{id}")
    public String deleteActivity(@PathVariable Long id) {
        activityService.delete(id);
        return "redirect:/admin/activities";
    }
}
