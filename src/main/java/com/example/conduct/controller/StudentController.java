package com.example.conduct.controller;

import com.example.conduct.entity.Student;
import com.example.conduct.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // LIST + FILTER
    @GetMapping
    public String listStudents(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Integer status,
                               Model model) {

        List<Student> students = studentService.getAllStudentsInClass(keyword, status);

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "admin/admin-students";
    }

    // SHOW FORM THÊM/SỬA (tuỳ bạn có dùng modal hay trang riêng)
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        return "admin/admin-student-edit";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:/admin/students";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/admin/students";
    }
}
