package com.example.conduct.service;

import com.example.conduct.entity.ClassEntity;
import com.example.conduct.entity.Student;
import com.example.conduct.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // LIST + FILTER
    public List<Student> searchStudents(String keyword, Integer statusFilter, Long classIdFilter) {
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }
        return studentRepository.searchStudents(keyword, statusFilter, classIdFilter);
    }

    public Student getById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Lưu (thêm / sửa)
    public Student save(Student student) {
        LocalDateTime now = LocalDateTime.now();

        if (student.getStudentId() == null) {
            // THÊM MỚI
            student.setCreatedAt(now);
            student.setUpdatedAt(now);
            return studentRepository.save(student);
        } else {
            // UPDATE: lấy bản gốc để tránh mất dữ liệu
            Student existing = studentRepository.findById(student.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

            existing.setMssv(student.getMssv());
            existing.setFullName(student.getFullName());
            existing.setCourseYear(student.getCourseYear());
            existing.setEmail(student.getEmail());
            existing.setAvatar(student.getAvatar());
            existing.setStatus(student.getStatus());

            if (student.getClassEntity() != null) {
                existing.setClassEntity(student.getClassEntity());
            }

            existing.setUpdatedAt(now);
            return studentRepository.save(existing);
        }
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public long countActiveStudents()   { return studentRepository.countByStatus(1); }
    public long countInactiveStudents() { return studentRepository.countByStatus(0); }
    public long countSuspendedStudents(){ return studentRepository.countByStatus(2); }
}
