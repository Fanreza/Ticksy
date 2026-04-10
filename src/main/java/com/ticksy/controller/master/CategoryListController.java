package com.ticksy.controller.master;

import com.ticksy.model.Category;
import com.ticksy.service.CategoryService;
import com.ticksy.util.AlertHelper;
import com.ticksy.util.ViewNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class CategoryListController implements Initializable {
    @FXML private TextField searchField;
    @FXML private TableView<Category> tableView;
    @FXML private TableColumn<Category, Long> colId;
    @FXML private TableColumn<Category, String> colName;
    @FXML private TableColumn<Category, String> colDescription;

    private final CategoryService service = new CategoryService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadData();
    }

    private void loadData() { tableView.setItems(FXCollections.observableArrayList(service.findAll())); }

    @FXML private void onSearch() { tableView.setItems(FXCollections.observableArrayList(service.search(searchField.getText()))); }

    @FXML private void onAdd() { ViewNavigator.loadView("/fxml/master/CategoryForm.fxml"); }

    @FXML private void onEdit() {
        Category selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a category to edit."); return; }
        CategoryFormController ctrl = ViewNavigator.loadViewAndGetController("/fxml/master/CategoryForm.fxml");
        ctrl.setCategory(selected);
    }

    @FXML private void onDelete() {
        Category selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertHelper.showError("No Selection", "Please select a category to delete."); return; }
        if (AlertHelper.showConfirmation("Confirm Delete", "Are you sure you want to delete '" + selected.getName() + "'?")) {
            service.delete(selected);
            loadData();
            AlertHelper.showInfo("Deleted", "Category deleted successfully.");
        }
    }
}
