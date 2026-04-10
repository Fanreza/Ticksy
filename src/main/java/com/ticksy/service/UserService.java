package com.ticksy.service;

import com.ticksy.model.User;
import com.ticksy.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository repository = new UserRepository();

    public List<User> findAll() { return repository.findAll(); }

    public Optional<User> findById(Long id) { return repository.findById(id); }

    public User save(User user) { return repository.save(user); }

    public void delete(User user) { repository.delete(user); }

    public List<User> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return repository.searchByName(keyword);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<User> findAgents() {
        return repository.findAgents();
    }
}
