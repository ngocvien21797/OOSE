package com.example.conduct.controller;

import com.example.conduct.entity.ClassEntity;
import com.example.conduct.service.ClassService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public String listClasses(Model model) {
        List<ClassEntity> classes = classService.getAllClasses();
        model.addAttribute("classes", classes);
        model.addAttribute("classForm", new ClassEntity());
        return "admin/admin-classes";
    }

    @PostMapping("/save")
    public String saveClass(@ModelAttribute("classForm") ClassEntity classForm) {
        classService.save(classForm);
        return "redirect:/admin/classes";
    }

    @GetMapping("/edit/{id}")
    public String editClass(@PathVariable Long id, Model model) {
        ClassEntity classEntity = classService.getById(id);
        model.addAttribute("classForm", classEntity);
        model.addAttribute("classes", classService.getAllClasses());
        return "admin/admin-classes";
    }

    @PostMapping("/delete/{id}")
    public String deleteClass(@PathVariable Long id) {
        classService.delete(id);
        return "redirect:/admin/classes";
    }
}
