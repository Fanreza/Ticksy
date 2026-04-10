package com.ticksy.controller.master;

import com.ticksy.model.Priority;
import com.ticksy.service.PriorityService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class PriorityListController implements Initializable {
    @FXML private TextField searchField;
    @FXML private TableView<Priority> tableView;
    @FXML private TableColumn<Priority, Long> colId;
    @FXML private TableColumn<Priority, String> colName;
    @FXML private TableColumn<Priority, Integer> colLevel;
    @FXML private TableColumn<Priority, String> colColor;

    private final PriorityService service = new PriorityService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        loadData();
    }

    private void loadData() { tableView.setItems(FXCollections.observableArrayList(service.findAll())); }
    @FXML private void onSearch() { tableView.setItems(FXCollections.observableArrayList(service.search(searchField.getText()))); }
    @FXML private void onAdd() { ViewNavigator.loadView("/fxml/master/PriorityForm.fxml"); }

    @FXML private void onEdit() {
        Priority selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a priority to edit."); return; }
        PriorityFormController ctrl = ViewNavigator.loadViewAndGetController("/fxml/master/PriorityForm.fxml");
        ctrl.setPriority(selected);
    }

    @FXML private void onDelete() {
        Priority selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a priority to delete."); return; }
        if (AlertHelper.showConfirmation("Confirm Delete", "Delete '" + selected.getName() + "'?")) {
            service.delete(selected);
            loadData();
        }
    }
}
