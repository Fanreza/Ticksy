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

public class TicketSummaryController implements Initializable {

    @FXML private TableView<Object[]> statusTable;
    @FXML private TableColumn<Object[], String> colStatusName;
    @FXML private TableColumn<Object[], String> colStatusCount;

    @FXML private TableView<Object[]> priorityTable;
    @FXML private TableColumn<Object[], String> colPriorityName;
    @FXML private TableColumn<Object[], String> colPriorityCount;

    @FXML private TableView<Object[]> categoryTable;
    @FXML private TableColumn<Object[], String> colCategoryName;
    @FXML private TableColumn<Object[], String> colCategoryCount;

    private final TicketService service = new TicketService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupSummaryTable(colStatusName, colStatusCount);
        setupSummaryTable(colPriorityName, colPriorityCount);
        setupSummaryTable(colCategoryName, colCategoryCount);

        statusTable.setItems(FXCollections.observableArrayList(service.getTicketCountByStatus()));
        priorityTable.setItems(FXCollections.observableArrayList(service.getTicketCountByPriority()));
        categoryTable.setItems(FXCollections.observableArrayList(service.getTicketCountByCategory()));
    }

    private void setupSummaryTable(TableColumn<Object[], String> nameCol, TableColumn<Object[], String> countCol) {
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue()[0])));
        countCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue()[1])));
    }
}
