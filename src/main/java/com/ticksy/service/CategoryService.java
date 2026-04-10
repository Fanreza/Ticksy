package com.ticksy.service;

import com.ticksy.model.Category;
import com.ticksy.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    private final CategoryRepository repository = new CategoryRepository();

    public List<Category> findAll() { return repository.findAll(); }

    public Optional<Category> findById(Long id) { return repository.findById(id); }

    public Category save(Category category) { return repository.save(category); }

    public void delete(Category category) { repository.delete(category); }

    public List<Category> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return repository.searchByName(keyword);
    }
}
