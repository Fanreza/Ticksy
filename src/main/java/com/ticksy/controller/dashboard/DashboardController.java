package com.ticksy.controller.dashboard;

import com.ticksy.model.Ticket;
import com.ticksy.service.TicketService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label lblTotalTickets;
    @FXML private Label lblOpenTickets;
    @FXML private Label lblInProgressTickets;
    @FXML private Label lblResolvedTickets;
    @FXML private Label lblClosedTickets;

    @FXML private TableView<Ticket> recentTicketsTable;
    @FXML private TableColumn<Ticket, String> colTicketNo;
    @FXML private TableColumn<Ticket, String> colTitle;
    @FXML private TableColumn<Ticket, String> colPriority;
    @FXML private TableColumn<Ticket, String> colStatus;
    @FXML private TableColumn<Ticket, String> colAssignee;
    @FXML private TableColumn<Ticket, String> colCreatedAt;

    private final TicketService ticketService = new TicketService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadData();
    }

    private void setupTable() {
        colTicketNo.setCellValueFactory(new PropertyValueFactory<>("ticketNumber"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        colPriority.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getPriority() != null ? data.getValue().getPriority().getName() : ""));

        colStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStatus() != null ? data.getValue().getStatus().getName() : ""));

        colAssignee.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAssignedTo() != null ? data.getValue().getAssignedTo().getFullName() : "Unassigned"));

        colCreatedAt.setCellValueFactory(data -> {
            LocalDateTime dt = data.getValue().getCreatedAt();
            String formatted = dt != null ? dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });
    }

    private void loadData() {
        List<Ticket> allTickets = ticketService.findAll();

        lblTotalTickets.setText(String.valueOf(allTickets.size()));
        lblOpenTickets.setText(String.valueOf(
                allTickets.stream().filter(t -> "OPEN".equals(t.getStatus().getName())).count()));
        lblInProgressTickets.setText(String.valueOf(
                allTickets.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus().getName())).count()));
        lblResolvedTickets.setText(String.valueOf(
                allTickets.stream().filter(t -> "RESOLVED".equals(t.getStatus().getName())).count()));
        lblClosedTickets.setText(String.valueOf(
                allTickets.stream().filter(t -> "CLOSED".equals(t.getStatus().getName())).count()));

        recentTicketsTable.setItems(FXCollections.observableArrayList(allTickets));
    }
}
