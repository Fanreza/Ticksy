package com.ticksy.service;

import com.ticksy.model.Priority;
import com.ticksy.repository.PriorityRepository;

import java.util.List;
import java.util.Optional;

public class PriorityService {

    private final PriorityRepository repository = new PriorityRepository();

    public List<Priority> findAll() { return repository.findAll(); }

    public Optional<Priority> findById(Long id) { return repository.findById(id); }

    public Priority save(Priority priority) { return repository.save(priority); }

    public void delete(Priority priority) { repository.delete(priority); }

    public List<Priority> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return repository.searchByName(keyword);
    }
}
