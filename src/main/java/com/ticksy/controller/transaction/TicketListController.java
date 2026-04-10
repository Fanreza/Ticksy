package com.ticksy.controller.transaction;

import com.ticksy.model.Ticket;
import com.ticksy.service.TicketService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.SessionManager;
import com.ticksy.util.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class TicketListController implements Initializable {

    @FXML private Label pageTitle;
    @FXML private TextField searchField;
    @FXML private TableView<Ticket> tableView;
    @FXML private TableColumn<Ticket, String> colTicketNo;
    @FXML private TableColumn<Ticket, String> colTitle;
    @FXML private TableColumn<Ticket, String> colCategory;
    @FXML private TableColumn<Ticket, String> colPriority;
    @FXML private TableColumn<Ticket, String> colStatus;
    @FXML private TableColumn<Ticket, String> colCreatedBy;
    @FXML private TableColumn<Ticket, String> colAssignedTo;
    @FXML private TableColumn<Ticket, String> colCreatedAt;

    @FXML private Button btnEdit;
    @FXML private Button btnClose;
    @FXML private Button btnDelete;

    private final TicketService service = new TicketService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupColumns();
        applyRoleRestrictions();
        loadData();
    }

    private void setupColumns() {
        colTicketNo.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCategory.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCategory() != null ? data.getValue().getCategory().getName() : ""));
        colPriority.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getPriority() != null ? data.getValue().getPriority().getName() : ""));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getStatus() != null ? data.getValue().getStatus().getName() : ""));
        colCreatedBy.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCreatedBy() != null ? data.getValue().getCreatedBy().getFullName() : ""));
        colAssignedTo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAssignedTo() != null ? data.getValue().getAssignedTo().getFullName() : "Unassigned"));
        colCreatedAt.setCellValueFactory(data -> {
            var dt = data.getValue().getCreatedAt();
            return new SimpleStringProperty(dt != null ? dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
        });
    }

    private void applyRoleRestrictions() {
        if (SessionManager.isUser()) {
            // USER: can only see own tickets, no close/delete, limited edit
            pageTitle.setText("My Tickets");
            btnClose.setVisible(false);
            btnClose.setManaged(false);
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
        } else if (SessionManager.isAgent()) {
            // AGENT: can see all, can close, no delete
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
        }
        // ADMIN: everything visible
    }

    private void loadData() {
        List<Ticket> tickets;

        if (SessionManager.isUser()) {
            // USER only sees their own tickets
            Long userId = SessionManager.getCurrentUser().getId();
            tickets = service.findAll().stream()
                    .filter(t -> t.getCreatedBy() != null && t.getCreatedBy().getId().equals(userId))
                    .toList();
        } else {
            tickets = service.findAll();
        }

        tableView.setItems(FXCollections.observableArrayList(tickets));
    }

    @FXML
    private void onSearch() {
        List<Ticket> results = service.search(searchField.getText());

        if (SessionManager.isUser()) {
            Long userId = SessionManager.getCurrentUser().getId();
            results = results.stream()
                    .filter(t -> t.getCreatedBy() != null && t.getCreatedBy().getId().equals(userId))
                    .toList();
        }

        tableView.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void onAdd() {
        ViewNavigator.loadView("/fxml/transaction/TicketForm.fxml");
    }

    @FXML
    private void onEdit() {
        Ticket selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("No Selection", "Please select a ticket to edit.");
            return;
        }
        TicketFormController ctrl = ViewNavigator.loadViewAndGetController("/fxml/transaction/TicketForm.fxml");
        ctrl.setTicket(selected);
    }

    @FXML
    private void onClose() {
        Ticket selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("No Selection", "Please select a ticket to close.");
            return;
        }
        if ("CLOSED".equals(selected.getStatus().getName())) {
            AlertHelper.showError("Already Closed", "This ticket is already closed.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Close Ticket");
        dialog.setHeaderText("Enter closing comment for " + selected.getTicketNumber());
        dialog.setContentText("Comment:");
        dialog.showAndWait().ifPresent(comment -> {
            service.closeTicket(selected, comment);
            loadData();
            AlertHelper.showInfo("Closed", "Ticket closed successfully.");
        });
    }

    @FXML
    private void onDelete() {
        Ticket selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("No Selection", "Please select a ticket to delete.");
            return;
        }
        if (AlertHelper.showConfirmation("Confirm Delete", "Delete ticket " + selected.getTicketNumber() + "?")) {
            service.delete(selected);
            loadData();
        }
    }
}
