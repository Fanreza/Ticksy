package com.ticksy.controller.transaction;

import com.ticksy.model.User;
import com.ticksy.service.UserService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class UserListController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, Long> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colFullName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colDepartment;
    @FXML private TableColumn<User, Boolean> colActive;

    private final UserService service = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRole() != null ? data.getValue().getRole().getName() : ""));
        colDepartment.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDepartment() != null ? data.getValue().getDepartment().getName() : ""));
        colActive.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isActive()).asObject());
        loadData();
    }

    private void loadData() { tableView.setItems(FXCollections.observableArrayList(service.findAll())); }

    @FXML private void onSearch() {
        tableView.setItems(FXCollections.observableArrayList(service.search(searchField.getText())));
    }

    @FXML private void onAdd() { ViewNavigator.loadView("/fxml/transaction/UserForm.fxml"); }

    @FXML private void onEdit() {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a user to edit."); return; }
        UserFormController ctrl = ViewNavigator.loadViewAndGetController("/fxml/transaction/UserForm.fxml");
        ctrl.setUser(selected);
    }

    @FXML private void onDelete() {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a user to delete."); return; }
        if (AlertHelper.showConfirmation("Confirm Delete", "Delete user '" + selected.getUsername() + "'?")) {
            service.delete(selected);
            loadData();
        }
    }
}
