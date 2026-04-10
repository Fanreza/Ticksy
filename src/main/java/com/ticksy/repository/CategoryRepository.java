package com.ticksy.repository;

import com.ticksy.model.Category;
import java.util.List;

public class CategoryRepository extends GenericRepository<Category> {

    public CategoryRepository() {
        super(Category.class);
    }

    public List<Category> searchByName(String keyword) {
        return findByQuery("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(?1)",
                "%" + keyword + "%");
    }
}
