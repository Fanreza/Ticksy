package com.ticksy.controller.report;

import com.ticksy.service.TicketService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class UserActivityController implements Initializable {

    @FXML private TableView<Object[]> tableView;
    @FXML private TableColumn<Object[], String> colUser;
    @FXML private TableColumn<Object[], String> colTickets;

    private final TicketService service = new TicketService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colUser.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue()[0])));
        colTickets.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue()[1])));
        tableView.setItems(FXCollections.observableArrayList(service.getUserActivity()));
    }
}
