package com.ticksy.service;

import com.ticksy.model.Status;
import com.ticksy.repository.StatusRepository;

import java.util.List;
import java.util.Optional;

public class StatusService {

    private final StatusRepository repository = new StatusRepository();

    public List<Status> findAll() { return repository.findAll(); }

    public Optional<Status> findById(Long id) { return repository.findById(id); }

    public Status save(Status status) { return repository.save(status); }

    public void delete(Status status) { repository.delete(status); }

    public List<Status> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return repository.searchByName(keyword);
    }
}
