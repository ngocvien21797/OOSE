package com.example.conduct.service;

import com.example.conduct.entity.ClassEntity;
import com.example.conduct.repository.ClassRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }

    public ClassEntity getById(Long id) {
        return classRepository.findById(id).orElse(null);
    }

    public ClassEntity save(ClassEntity classEntity) {
        LocalDateTime now = LocalDateTime.now();

        if (classEntity.getClassId() == null) {
            // thêm mới
            classEntity.setCreatedAt(now);
        }
        // thêm mới hoặc update đều set updatedAt
        classEntity.setUpdatedAt(now);

        return classRepository.save(classEntity);
    }

    public void delete(Long id) {
        classRepository.deleteById(id);
    }
}
