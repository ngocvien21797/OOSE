package com.example.conduct.controller;

import com.example.conduct.entity.User;
import com.example.conduct.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ===== WEB LOGIN (Thymeleaf, dùng bảng users) =====
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";  // templates/login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        String u = username == null ? null : username.trim();
        String p = password == null ? null : password.trim();

        User user = authService.login(u, p);

        if (user == null) {
            model.addAttribute("errorMessage", "Sai tài khoản hoặc mật khẩu");
            return "login";
        }

        session.setAttribute("currentUser", user);

        // Điều hướng theo ENUM role trong DB
        switch (user.getRole()) {
            case ADMIN -> {
                return "redirect:/admin/dashboard";
            }
            case SECRETARY -> {
                return "redirect:/secretary/dashboard";
            }
            case CLASS_LEADER -> {
                return "redirect:/class-leader/dashboard";
            }
            case STUDENT -> {
                return "redirect:/students/dashboard";
            }
            default -> {
                return "redirect:/login";
            }
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ===== WEB REGISTER (tạo tài khoản STUDENT) =====

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // có thể truyền sẵn attribute nếu cần, tạm thời để trống
        return "/register"; // templates/register.html
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam("confirmPassword") String confirmPassword,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        String u = username == null ? null : username.trim();
        String e = email == null ? null : email.trim();
        String p = password == null ? null : password.trim();
        String cp = confirmPassword == null ? null : confirmPassword.trim();

        // Validate cơ bản
        if (u == null || u.isEmpty()
                || e == null || e.isEmpty()
                || p == null || p.isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin");
            model.addAttribute("username", u);
            model.addAttribute("email", e);
            return "/register";
        }

        if (!p.equals(cp)) {
            model.addAttribute("errorMessage", "Mật khẩu nhập lại không khớp");
            model.addAttribute("username", u);
            model.addAttribute("email", e);
            return "/register";
        }

        try {
            authService.registerStudent(u, e, p);
            // dùng FlashAttribute để show message ở trang login
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công, mời bạn đăng nhập.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("username", u);
            model.addAttribute("email", e);
            return "/register";
        }
    }

    // ===== API LOGIN (JWT, dùng bảng users) =====
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> apiLogin(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        try {
            String token = authService.loginAndGenerateToken(username, password);
            User user = authService.findByUsername(username);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", user.getUsername(),
                    "role", user.getRole().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid username or password"
            ));
        }
    }
}
