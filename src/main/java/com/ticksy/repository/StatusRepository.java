package com.ticksy.repository;

import com.ticksy.model.Status;
import java.util.List;

public class StatusRepository extends GenericRepository<Status> {

    public StatusRepository() {
        super(Status.class);
    }

    public List<Status> searchByName(String keyword) {
        return findByQuery("SELECT s FROM Status s WHERE LOWER(s.name) LIKE LOWER(?1)",
                "%" + keyword + "%");
    }
}
