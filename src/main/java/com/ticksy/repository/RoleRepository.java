package com.ticksy.repository;

import com.ticksy.model.Role;
import java.util.List;

public class RoleRepository extends GenericRepository<Role> {

    public RoleRepository() {
        super(Role.class);
    }

    public List<Role> searchByName(String keyword) {
        return findByQuery("SELECT r FROM Role r WHERE LOWER(r.name) LIKE LOWER(?1)",
                "%" + keyword + "%");
    }
}
