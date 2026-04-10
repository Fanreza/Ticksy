package com.ticksy.service;

import com.ticksy.model.Role;
import com.ticksy.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

public class RoleService {

    private final RoleRepository repository = new RoleRepository();

    public List<Role> findAll() { return repository.findAll(); }

    public Optional<Role> findById(Long id) { return repository.findById(id); }

    public Role save(Role role) { return repository.save(role); }

    public void delete(Role role) { repository.delete(role); }

    public List<Role> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return repository.searchByName(keyword);
    }
}
