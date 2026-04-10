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

public class ResolutionTimeController implements Initializable {

    @FXML private TableView<Object[]> tableView;
    @FXML private TableColumn<Object[], String> colAgent;
    @FXML private TableColumn<Object[], String> colAvgHours;

    private final TicketService service = new TicketService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colAgent.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue()[0])));
        colAvgHours.setCellValueFactory(data -> {
            Object val = data.getValue()[1];
            String formatted = val != null ? String.format("%.1f", ((Number) val).doubleValue()) : "N/A";
            return new SimpleStringProperty(formatted);
        });
        tableView.setItems(FXCollections.observableArrayList(service.getAverageResolutionTime()));
    }
}
