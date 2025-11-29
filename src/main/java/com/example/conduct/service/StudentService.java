package com.example.conduct.service;

import com.example.conduct.entity.Student;
import com.example.conduct.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Lấy danh sách sinh viên, có filter theo keyword + status.
     * keyword: tìm theo tên hoặc MSSV
     * status: 0/1/2 hoặc null = tất cả
     */
    public List<Student> getAllStudentsInClass(String keyword, Integer status) {
        // nếu chuỗi rỗng thì coi như null cho gọn
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }
        return studentRepository.searchStudents(keyword, status);
    }

    public Student getById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student save(Student student) {
        // TODO: nếu cần set createdAt/updatedAt thì handle ở đây
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    // mấy hàm này dùng cho dashboard sau
    public long countActiveStudents() {
        return studentRepository.countByStatus(1);
    }

    public long countInactiveStudents() {
        return studentRepository.countByStatus(0);
    }

    public long countSuspendedStudents() {
        return studentRepository.countByStatus(2);
    }
}
