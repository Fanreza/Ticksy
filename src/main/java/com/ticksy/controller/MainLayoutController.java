package com.ticksy.controller;

import com.ticksy.util.SessionManager;
import com.ticksy.util.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML private StackPane contentArea;
    @FXML private MenuBar menuBar;
    @FXML private Label lblCurrentUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ViewNavigator.setContentArea(contentArea);

        lblCurrentUser.setText(SessionManager.getCurrentUser().getFullName()
                + " (" + SessionManager.getCurrentRole() + ")");

        buildMenus();

        if (SessionManager.isUser()) {
            ViewNavigator.loadView("/fxml/transaction/TicketList.fxml");
        } else {
            ViewNavigator.loadView("/fxml/dashboard/Dashboard.fxml");
        }
    }

    private void buildMenus() {
        menuBar.getMenus().clear();

        if (!SessionManager.isUser()) {
            Menu dashboard = new Menu("Dashboard");
            MenuItem overview = new MenuItem("Overview");
            overview.setOnAction(e -> onDashboard());
            dashboard.getItems().add(overview);
            menuBar.getMenus().add(dashboard);
        }

        if (SessionManager.canAccessMasterData()) {
            Menu master = new Menu("Master");
            master.getItems().addAll(
                    menuItem("Department", this::onDepartments),
                    menuItem("Category", this::onCategories),
                    menuItem("Priority", this::onPriorities),
                    menuItem("Status", this::onStatuses),
                    menuItem("Role", this::onRoles)
            );
            menuBar.getMenus().add(master);
        }

        Menu transactions = new Menu("Transactions");

        if (SessionManager.canManageUsers()) {
            transactions.getItems().add(menuItem("Users", this::onUsers));
        }

        transactions.getItems().add(menuItem("Tickets", this::onTickets));

        if (SessionManager.canAssignTickets()) {
            transactions.getItems().add(menuItem("Assign Ticket", this::onAssignTicket));
        }

        menuBar.getMenus().add(transactions);

        if (SessionManager.canAccessReports()) {
            Menu reports = new Menu("Reports");
            reports.getItems().addAll(
                    menuItem("Ticket Summary", this::onReportSummary),
                    menuItem("Agent Performance", this::onReportAgentPerf),
                    menuItem("Average Resolution Time", this::onReportResolutionTime),
                    menuItem("Tickets Created Over Time", this::onReportOverTime),
                    menuItem("User Activity", this::onReportUserActivity)
            );
            menuBar.getMenus().add(reports);
        }
    }

    private MenuItem menuItem(String text, Runnable action) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(e -> action.run());
        return item;
    }

    @FXML
    private void onLogout() {
        SessionManager.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/layout/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setMaximized(false);
            Scene scene = new Scene(root, 500, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setTitle("Ticksy - Sign In");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load login screen", e);
        }
    }

    private void onDashboard() { ViewNavigator.loadView("/fxml/dashboard/Dashboard.fxml"); }

    private void onDepartments() { ViewNavigator.loadView("/fxml/master/DepartmentList.fxml"); }
    private void onCategories()  { ViewNavigator.loadView("/fxml/master/CategoryList.fxml"); }
    private void onPriorities()  { ViewNavigator.loadView("/fxml/master/PriorityList.fxml"); }
    private void onStatuses()    { ViewNavigator.loadView("/fxml/master/StatusList.fxml"); }
    private void onRoles()       { ViewNavigator.loadView("/fxml/master/RoleList.fxml"); }

    private void onUsers()        { ViewNavigator.loadView("/fxml/transaction/UserList.fxml"); }
    private void onTickets()      { ViewNavigator.loadView("/fxml/transaction/TicketList.fxml"); }
    private void onAssignTicket() { ViewNavigator.loadView("/fxml/transaction/AssignTicket.fxml"); }

    private void onReportSummary()        { ViewNavigator.loadView("/fxml/report/TicketSummary.fxml"); }
    private void onReportAgentPerf()      { ViewNavigator.loadView("/fxml/report/AgentPerformance.fxml"); }
    private void onReportResolutionTime() { ViewNavigator.loadView("/fxml/report/ResolutionTime.fxml"); }
    private void onReportOverTime()       { ViewNavigator.loadView("/fxml/report/TicketsOverTime.fxml"); }
    private void onReportUserActivity()   { ViewNavigator.loadView("/fxml/report/UserActivity.fxml"); }
}
