package com.example.conduct.controller;

import com.example.conduct.entity.User;
import com.example.conduct.entity.User.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (currentUser.getRole() != Role.ADMIN) {
            // không phải admin thì đá ra login (hoặc bạn đổi sang trang khác cũng được)
            return "redirect:/login";
        }

        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("role", currentUser.getRole().name());

        return "admin/dashboard-admin"; // templates/admin/dashboard-admin.html
    }

    @GetMapping("/students/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        // tuỳ bạn: cho tất cả role vào dashboard-students luôn
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("role", currentUser.getRole().name());

        return "students/dashboard-student"; // templates/students/dashboard-students.html
    }
}
