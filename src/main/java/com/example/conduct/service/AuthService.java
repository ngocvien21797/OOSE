package com.example.conduct.service;

import com.example.conduct.entity.User;
import com.example.conduct.repository.UserRepository;
import com.example.conduct.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // ---- LOGIN WEB (trả về User, dùng cho Thymeleaf) ----
    public User login(String username, String rawPassword) {
        if (username == null || rawPassword == null) {
            return null;
        }

        String u = username.trim();
        String p = rawPassword.trim();

        Optional<User> opt = userRepository.findByUsername(u);
        if (opt.isEmpty()) {
            return null;
        }

        User user = opt.get();

        // 0 = inactive
        if (user.getStatus() == 0) {
            return null;
        }

        if (!passwordEncoder.matches(p, user.getPassword())) {
            return null;
        }

        return user;
    }

    // ---- Đăng ký STUDENT (dùng cho form register) ----
    public User registerStudent(String username, String email, String rawPassword) {
        String u = username == null ? null : username.trim();
        String e = email == null ? null : email.trim();
        String p = rawPassword == null ? null : rawPassword.trim();

        if (u == null || u.isEmpty() ||
            e == null || e.isEmpty() ||
            p == null || p.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin");
        }

        if (userRepository.existsByUsername(u)) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(e)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(u);
        user.setEmail(e);
        user.setPassword(passwordEncoder.encode(p));
        user.setRole(User.Role.STUDENT);   // enum STUDENT trong User
        user.setStatus(1);                 // 1 = active
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // ---- LOGIN API + JWT ----
    public String loginAndGenerateToken(String username, String rawPassword) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, rawPassword);

        authenticationManager.authenticate(authToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
