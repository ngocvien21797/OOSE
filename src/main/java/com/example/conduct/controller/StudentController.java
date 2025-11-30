package com.example.conduct.controller;

import com.example.conduct.entity.ClassEntity;
import com.example.conduct.entity.Student;
import com.example.conduct.service.ClassService;
import com.example.conduct.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/students")
public class StudentController {

    private final StudentService studentService;
    private final ClassService classService;

    public StudentController(StudentService studentService, ClassService classService) {
        this.studentService = studentService;
        this.classService = classService;
    }

    // ===== LIST + FILTER =====
    @GetMapping
    public String listStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, name = "statusFilter") Integer statusFilter,
            @RequestParam(required = false, name = "classIdFilter") Long classIdFilter,
            Model model
    ) {
        // dropdown lớp
        model.addAttribute("classes", classService.getAllClasses());

        // filter sinh viên
        List<Student> students = studentService.searchStudents(keyword, statusFilter, classIdFilter);

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("classIdFilter", classIdFilter);

        return "admin/admin-students";
    }

    // ===== FORM THÊM SINH VIÊN (TRANG RIÊNG) =====
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // object rỗng để binding form
        model.addAttribute("student", new Student());
        // list lớp cho combobox
        model.addAttribute("classes", classService.getAllClasses());
        // có thể set thêm "formMode" nếu HTML cần phân biệt create/edit
        model.addAttribute("formMode", "create");
        return "admin/admin-student-create"; // nhớ tạo file này trong templates/admin
    }

    // ===== LƯU (THÊM hoặc SỬA) – dùng chung endpoint /save =====
    @PostMapping("/save")
    public String saveStudent(
            @ModelAttribute Student student,
            @RequestParam Long classId
    ) {
        ClassEntity classEntity = classService.getById(classId);
        student.setClassEntity(classEntity);

        // Thời gian tạo/cập nhật xử lý trong service
        studentService.save(student);
        return "redirect:/admin/students";
    }

    // ===== FORM CHỈNH SỬA (LOAD DỮ LIỆU CŨ) =====
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getById(id);

        model.addAttribute("student", student);
        model.addAttribute("classes", classService.getAllClasses());
        model.addAttribute("formMode", "edit"); // báo HTML biết đang sửa
        return "admin/admin-student-edit";
    }

    // ===== XOÁ =====
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/admin/students";
    }
}
