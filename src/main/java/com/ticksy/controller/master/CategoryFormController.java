package com.ticksy.controller.master;

import com.ticksy.model.Category;
import com.ticksy.service.CategoryService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CategoryFormController {
    @FXML private Label formTitle;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;

    private final CategoryService service = new CategoryService();
    private Category category;

    public void setCategory(Category category) {
        this.category = category;
        formTitle.setText("Edit Category");
        nameField.setText(category.getName());
        descriptionField.setText(category.getDescription());
    }

    @FXML private void onSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { AlertHelper.showError("Validation Error", "Name is required."); return; }
        if (category == null) category = new Category();
        category.setName(name);
        category.setDescription(descriptionField.getText().trim());
        try {
            service.save(category);
            AlertHelper.showInfo("Success", "Category saved successfully.");
            ViewNavigator.loadView("/fxml/master/CategoryList.fxml");
        } catch (Exception e) { AlertHelper.showError("Error", "Failed to save: " + e.getMessage()); }
    }

    @FXML private void onCancel() { ViewNavigator.loadView("/fxml/master/CategoryList.fxml"); }
}
