package com.ticksy.service;

import com.ticksy.model.Department;
import com.ticksy.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

public class DepartmentService {

    private final DepartmentRepository repository = new DepartmentRepository();

    public List<Department> findAll() {
        return repository.findAll();
    }

    public Optional<Department> findById(Long id) {
        return repository.findById(id);
    }

    public Department save(Department department) {
        return repository.save(department);
    }

    public void delete(Department department) {
        repository.delete(department);
    }

    public List<Department> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return repository.searchByName(keyword);
    }
}
