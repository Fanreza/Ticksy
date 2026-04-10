package com.ticksy.repository;

import com.ticksy.model.User;
import java.util.List;
import java.util.Optional;

public class UserRepository extends GenericRepository<User> {

    public UserRepository() {
        super(User.class);
    }

    public List<User> searchByName(String keyword) {
        return findByQuery(
                "SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(?1) OR LOWER(u.username) LIKE LOWER(?1)",
                "%" + keyword + "%");
    }

    public Optional<User> findByUsername(String username) {
        List<User> results = findByQuery("SELECT u FROM User u WHERE u.username = ?1", username);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<User> findByRoleName(String roleName) {
        return findByQuery("SELECT u FROM User u WHERE u.role.name = ?1", roleName);
    }

    public List<User> findAgents() {
        return findByRoleName("AGENT");
    }
}
