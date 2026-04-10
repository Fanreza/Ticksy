package com.ticksy.controller.master;

import com.ticksy.model.Priority;
import com.ticksy.service.PriorityService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class PriorityFormController implements Initializable {
    @FXML private Label formTitle;
    @FXML private TextField nameField;
    @FXML private Spinner<Integer> levelSpinner;
    @FXML private TextField colorField;

    private final PriorityService service = new PriorityService();
    private Priority priority;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        levelSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        formTitle.setText("Edit Priority");
        nameField.setText(priority.getName());
        levelSpinner.getValueFactory().setValue(priority.getLevel());
        colorField.setText(priority.getColor());
    }

    @FXML private void onSave() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { AlertHelper.showError("Validation Error", "Name is required."); return; }
        if (priority == null) priority = new Priority();
        priority.setName(name);
        priority.setLevel(levelSpinner.getValue());
        priority.setColor(colorField.getText().trim());
        try {
            service.save(priority);
            AlertHelper.showInfo("Success", "Priority saved successfully.");
            ViewNavigator.loadView("/fxml/master/PriorityList.fxml");
        } catch (Exception e) { AlertHelper.showError("Error", "Failed to save: " + e.getMessage()); }
    }

    @FXML private void onCancel() { ViewNavigator.loadView("/fxml/master/PriorityList.fxml"); }
}
