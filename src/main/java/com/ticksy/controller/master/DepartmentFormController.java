package com.ticksy.controller.master;

import com.ticksy.model.Department;
import com.ticksy.service.DepartmentService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DepartmentFormController {

    @FXML private Label formTitle;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;

    private final DepartmentService service = new DepartmentService();
    private Department department;

    public void setDepartment(Department department) {
        this.department = department;
        formTitle.setText("Edit Department");
        nameField.setText(department.getName());
        descriptionField.setText(department.getDescription());
    }

    @FXML
    private void onSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            AlertHelper.showError("Validation Error", "Name is required.");
            return;
        }

        if (department == null) {
            department = new Department();
        }
        department.setName(name);
        department.setDescription(descriptionField.getText().trim());

        try {
            service.save(department);
            AlertHelper.showInfo("Success", "Department saved successfully.");
            ViewNavigator.loadView("/fxml/master/DepartmentList.fxml");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to save department: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        ViewNavigator.loadView("/fxml/master/DepartmentList.fxml");
    }
}
