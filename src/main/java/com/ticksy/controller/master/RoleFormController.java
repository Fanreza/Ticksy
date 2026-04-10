package com.ticksy.controller.master;

import com.ticksy.model.Role;
import com.ticksy.service.RoleService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RoleFormController {
    @FXML private Label formTitle;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;

    private final RoleService service = new RoleService();
    private Role role;

    public void setRole(Role role) {
        this.role = role;
        formTitle.setText("Edit Role");
        nameField.setText(role.getName());
        descriptionField.setText(role.getDescription());
    }

    @FXML private void onSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { AlertHelper.showError("Validation Error", "Name is required."); return; }
        if (role == null) role = new Role();
        role.setName(name);
        role.setDescription(descriptionField.getText().trim());
        try {
            service.save(role);
            AlertHelper.showInfo("Success", "Role saved successfully.");
            ViewNavigator.loadView("/fxml/master/RoleList.fxml");
        } catch (Exception e) { AlertHelper.showError("Error", "Failed to save: " + e.getMessage()); }
    }

    @FXML private void onCancel() { ViewNavigator.loadView("/fxml/master/RoleList.fxml"); }
}
