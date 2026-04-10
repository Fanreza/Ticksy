package com.ticksy.controller.master;

import com.ticksy.model.Status;
import com.ticksy.service.StatusService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class StatusListController implements Initializable {
    @FXML private TextField searchField;
    @FXML private TableView<Status> tableView;
    @FXML private TableColumn<Status, Long> colId;
    @FXML private TableColumn<Status, String> colName;
    @FXML private TableColumn<Status, String> colDescription;

    private final StatusService service = new StatusService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadData();
    }

    private void loadData() { tableView.setItems(FXCollections.observableArrayList(service.findAll())); }
    @FXML private void onSearch() { tableView.setItems(FXCollections.observableArrayList(service.search(searchField.getText()))); }
    @FXML private void onAdd() { ViewNavigator.loadView("/fxml/master/StatusForm.fxml"); }

    @FXML private void onEdit() {
        Status selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a status to edit."); return; }
        StatusFormController ctrl = ViewNavigator.loadViewAndGetController("/fxml/master/StatusForm.fxml");
        ctrl.setStatus(selected);
    }

    @FXML private void onDelete() {
        Status selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a status to delete."); return; }
        if (AlertHelper.showConfirmation("Confirm Delete", "Delete '" + selected.getName() + "'?")) {
            service.delete(selected);
            loadData();
        }
    }
}
