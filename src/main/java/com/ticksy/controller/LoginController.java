package com.ticksy.controller;

import com.ticksy.model.User;
import com.ticksy.service.UserService;
import com.ticksy.util.SessionManager;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private VBox loginCard;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playEntryAnimation();
    }

    private void playEntryAnimation() {
        loginCard.setTranslateY(40);
        loginCard.setOpacity(0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(600), loginCard);
        slide.setFromY(40);
        slide.setToY(0);

        FadeTransition fade = new FadeTransition(Duration.millis(600), loginCard);
        fade.setFromValue(0);
        fade.setToValue(1);

        slide.play();
        fade.play();
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            showError("Invalid username or password.");
            return;
        }

        User user = userOpt.get();

        if (!user.getPasswordHash().equals(password)) {
            showError("Invalid username or password.");
            return;
        }

        if (!user.isActive()) {
            showError("Your account has been deactivated. Contact admin.");
            return;
        }

        SessionManager.setCurrentUser(user);
        playExitAnimation();
    }

    private void showError(String message) {
        errorLabel.setText(message);

        TranslateTransition shake = new TranslateTransition(Duration.millis(60), loginCard);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> loginCard.setTranslateX(0));
        shake.play();
    }

    private void playExitAnimation() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(350), loginCard);
        scale.setToX(1.03);
        scale.setToY(1.03);

        FadeTransition fade = new FadeTransition(Duration.millis(350), loginCard);
        fade.setToValue(0);

        fade.setOnFinished(e -> loadMainLayout());
        scale.play();
        fade.play();
    }

    private void loadMainLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/layout/MainLayout.fxml"));
            Parent root = loader.load();

            root.setOpacity(0);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Ticksy - IT Helpdesk");
            stage.setResizable(true);
            stage.setMaximized(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        } catch (Exception e) {
            errorLabel.setText("Failed to load application: " + e.getMessage());
        }
    }
}
