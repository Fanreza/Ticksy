package com.ticksy.controller.transaction;

import com.ticksy.model.Ticket;
import com.ticksy.model.User;
import com.ticksy.service.TicketService;
import com.ticksy.service.UserService;
import com.ticksy.util.AlertHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AssignTicketController implements Initializable {

    @FXML private ComboBox<Ticket> ticketCombo;
    @FXML private ComboBox<User> agentCombo;
    @FXML private TableView<Ticket> tableView;
    @FXML private TableColumn<Ticket, String> colTicketNo;
    @FXML private TableColumn<Ticket, String> colTitle;
    @FXML private TableColumn<Ticket, String> colAgent;
    @FXML private TableColumn<Ticket, String> colStatus;

    private final TicketService ticketService = new TicketService();
    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colTicketNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTicketNumber()));
        colTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        colAgent.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAssignedTo() != null ? data.getValue().getAssignedTo().getFullName() : ""));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getStatus() != null ? data.getValue().getStatus().getName() : ""));

        loadData();
    }

    private void loadData() {
        ticketCombo.setItems(FXCollections.observableArrayList(ticketService.findUnassigned()));
        agentCombo.setItems(FXCollections.observableArrayList(userService.findAgents()));

        // Show all tickets that have been assigned
        var allTickets = ticketService.findAll().stream()
                .filter(t -> t.getAssignedTo() != null)
                .toList();
        tableView.setItems(FXCollections.observableArrayList(allTickets));
    }

    @FXML
    private void onAssign() {
        Ticket ticket = ticketCombo.getValue();
        User agent = agentCombo.getValue();

        if (ticket == null || agent == null) {
            AlertHelper.showError("Validation Error", "Please select both a ticket and an agent.");
            return;
        }

        try {
            ticketService.assignTicket(ticket, agent);
            AlertHelper.showInfo("Success", "Ticket " + ticket.getTicketNumber() + " assigned to " + agent.getFullName());
            loadData();
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to assign ticket: " + e.getMessage());
        }
    }
}
