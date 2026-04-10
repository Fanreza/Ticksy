package com.ticksy.controller.master;

import com.ticksy.model.Status;
import com.ticksy.service.StatusService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StatusFormController {
    @FXML private Label formTitle;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;

    private final StatusService service = new StatusService();
    private Status status;

    public void setStatus(Status status) {
        this.status = status;
        formTitle.setText("Edit Status");
        nameField.setText(status.getName());
        descriptionField.setText(status.getDescription());
    }

    @FXML private void onSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { AlertHelper.showError("Validation Error", "Name is required."); return; }
        if (status == null) status = new Status();
        status.setName(name);
        status.setDescription(descriptionField.getText().trim());
        try {
            service.save(status);
            AlertHelper.showInfo("Success", "Status saved successfully.");
            ViewNavigator.loadView("/fxml/master/StatusList.fxml");
        } catch (Exception e) { AlertHelper.showError("Error", "Failed to save: " + e.getMessage()); }
    }

    @FXML private void onCancel() { ViewNavigator.loadView("/fxml/master/StatusList.fxml"); }
}
