package com.ticksy.controller.transaction;

import com.ticksy.model.*;
import com.ticksy.service.*;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.SessionManager;
import com.ticksy.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketFormController implements Initializable {

    @FXML private Label formTitle;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<Category> categoryCombo;
    @FXML private ComboBox<Priority> priorityCombo;
    @FXML private ComboBox<User> createdByCombo;

    private final TicketService ticketService = new TicketService();
    private final CategoryService categoryService = new CategoryService();
    private final PriorityService priorityService = new PriorityService();
    private final UserService userService = new UserService();
    private Ticket ticket;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryCombo.setItems(FXCollections.observableArrayList(categoryService.findAll()));
        priorityCombo.setItems(FXCollections.observableArrayList(priorityService.findAll()));

        if (SessionManager.isUser()) {
            // USER: auto-set createdBy to themselves, lock the field
            createdByCombo.setItems(FXCollections.observableArrayList(SessionManager.getCurrentUser()));
            createdByCombo.setValue(SessionManager.getCurrentUser());
            createdByCombo.setDisable(true);
        } else {
            // ADMIN/AGENT: can pick any user
            createdByCombo.setItems(FXCollections.observableArrayList(userService.findAll()));
        }
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        formTitle.setText("Edit Ticket - " + ticket.getTicketNumber());
        titleField.setText(ticket.getTitle());
        descriptionField.setText(ticket.getDescription());
        categoryCombo.setValue(ticket.getCategory());
        priorityCombo.setValue(ticket.getPriority());
        createdByCombo.setValue(ticket.getCreatedBy());
        createdByCombo.setDisable(true);
    }

    @FXML
    private void onSave() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            AlertHelper.showError("Validation Error", "Title is required.");
            return;
        }
        if (categoryCombo.getValue() == null || priorityCombo.getValue() == null || createdByCombo.getValue() == null) {
            AlertHelper.showError("Validation Error", "Category, Priority, and Created By are required.");
            return;
        }

        boolean isNew = (ticket == null);
        if (isNew) ticket = new Ticket();

        ticket.setTitle(title);
        ticket.setDescription(descriptionField.getText().trim());
        ticket.setCategory(categoryCombo.getValue());
        ticket.setPriority(priorityCombo.getValue());
        ticket.setCreatedBy(createdByCombo.getValue());

        try {
            if (isNew) {
                ticketService.createTicket(ticket);
            } else {
                ticketService.save(ticket);
            }
            AlertHelper.showInfo("Success", "Ticket saved successfully.");
            ViewNavigator.loadView("/fxml/transaction/TicketList.fxml");
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to save ticket: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        ViewNavigator.loadView("/fxml/transaction/TicketList.fxml");
    }
}
