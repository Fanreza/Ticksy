package com.ticksy.controller.transaction;

import com.ticksy.model.Department;
import com.ticksy.model.Role;
import com.ticksy.model.User;
import com.ticksy.service.DepartmentService;
import com.ticksy.service.RoleService;
import com.ticksy.service.UserService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class UserFormController implements Initializable {

    @FXML private Label formTitle;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<Role> roleCombo;
    @FXML private ComboBox<Department> departmentCombo;
    @FXML private CheckBox activeCheck;

    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private final DepartmentService departmentService = new DepartmentService();
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roleCombo.setItems(FXCollections.observableArrayList(roleService.findAll()));
        departmentCombo.setItems(FXCollections.observableArrayList(departmentService.findAll()));
    }

    public void setUser(User user) {
        this.user = user;
        formTitle.setText("Edit User");
        usernameField.setText(user.getUsername());
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        roleCombo.setValue(user.getRole());
        departmentCombo.setValue(user.getDepartment());
        activeCheck.setSelected(user.isActive());
        passwordField.setPromptText("Leave blank to keep current password");
    }

    @FXML
    private void onSave() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            AlertHelper.showError("Validation Error", "Username, Full Name, and Email are required.");
            return;
        }
        if (roleCombo.getValue() == null) {
            AlertHelper.showError("Validation Error", "Role is required.");
            return;
        }
        if (user == null && password.isEmpty()) {
            AlertHelper.showError("Validation Error", "Password is required for new users.");
            return;
        }

        if (user == null) user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phoneField.getText().trim());
        user.setRole(roleCombo.getValue());
        user.setDepartment(departmentCombo.getValue());
        user.setActive(activeCheck.isSelected());

        if (!password.isEmpty()) {
            user.setPasswordHash(password); // In production, hash this
        }

        try {
            userService.save(user);
            AlertHelper.showInfo("Success", "User saved successfully.");
            ViewNavigator.loadView("/fxml/transaction/UserList.fxml");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to save user: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() { ViewNavigator.loadView("/fxml/transaction/UserList.fxml"); }
}
