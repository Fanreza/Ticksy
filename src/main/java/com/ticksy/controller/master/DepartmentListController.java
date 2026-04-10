package com.ticksy.controller.master;

import com.ticksy.model.Department;
import com.ticksy.service.DepartmentService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Department> tableView;
    @FXML private TableColumn<Department, Long> colId;
    @FXML private TableColumn<Department, String> colName;
    @FXML private TableColumn<Department, String> colDescription;

    private final DepartmentService service = new DepartmentService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadData();
    }

    private void loadData() {
        tableView.setItems(FXCollections.observableArrayList(service.findAll()));
    }

    @FXML
    private void onSearch() {
        String keyword = searchField.getText();
        tableView.setItems(FXCollections.observableArrayList(service.search(keyword)));
    }

    @FXML
    private void onAdd() {
        ViewNavigator.loadView("/fxml/master/DepartmentForm.fxml");
    }

    @FXML
    private void onEdit() {
        Department selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("No Selection", "Please select a department to edit.");
            return;
        }
        DepartmentFormController controller = ViewNavigator.loadViewAndGetController(
                "/fxml/master/DepartmentForm.fxml");
        controller.setDepartment(selected);
    }

    @FXML
    private void onDelete() {
        Department selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("No Selection", "Please select a department to delete.");
            return;
        }
        if (AlertHelper.showConfirmation("Confirm Delete",
                "Are you sure you want to delete '" + selected.getName() + "'?")) {
            service.delete(selected);
            loadData();
            AlertHelper.showInfo("Deleted", "Department deleted successfully.");
        }
    }
}
