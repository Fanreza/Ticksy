package com.ticksy.repository;

import com.ticksy.model.Priority;
import java.util.List;

public class PriorityRepository extends GenericRepository<Priority> {

    public PriorityRepository() {
        super(Priority.class);
    }

    public List<Priority> searchByName(String keyword) {
        return findByQuery("SELECT p FROM Priority p WHERE LOWER(p.name) LIKE LOWER(?1)",
                "%" + keyword + "%");
    }
}
