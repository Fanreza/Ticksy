package com.ticksy.repository;

import com.ticksy.model.Department;
import java.util.List;

public class DepartmentRepository extends GenericRepository<Department> {

    public DepartmentRepository() {
        super(Department.class);
    }

    public List<Department> searchByName(String keyword) {
        return findByQuery("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(?1)",
                "%" + keyword + "%");
    }
}
