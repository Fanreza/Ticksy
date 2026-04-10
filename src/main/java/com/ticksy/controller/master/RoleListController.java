package com.ticksy.controller.master;

import com.ticksy.model.Role;
import com.ticksy.service.RoleService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class RoleListController implements Initializable {
    @FXML private TextField searchField;
    @FXML private TableView<Role> tableView;
    @FXML private TableColumn<Role, Long> colId;
    @FXML private TableColumn<Role, String> colName;
    @FXML private TableColumn<Role, String> colDescription;

    private final RoleService service = new RoleService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadData();
    }

    private void loadData() { tableView.setItems(FXCollections.observableArrayList(service.findAll())); }
    @FXML private void onSearch() { tableView.setItems(FXCollections.observableArrayList(service.search(searchField.getText()))); }
    @FXML private void onAdd() { ViewNavigator.loadView("/fxml/master/RoleForm.fxml"); }

    @FXML private void onEdit() {
        Role selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a role to edit."); return; }
        RoleFormController ctrl = ViewNavigator.loadViewAndGetController("/fxml/master/RoleForm.fxml");
        ctrl.setRole(selected);
    }

    @FXML private void onDelete() {
        Role selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a role to delete."); return; }
        if (AlertHelper.showConfirmation("Confirm Delete", "Delete '" + selected.getName() + "'?")) {
            service.delete(selected);
            loadData();
        }
    }
}
