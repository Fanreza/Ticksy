package com.ticksy;

import atlantafx.base.theme.CupertinoDark;
import com.ticksy.config.HibernateUtil;
import com.ticksy.config.DataSeeder;
import com.ticksy.util.DarkTitleBar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TicksyApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());

        // Seed default data (roles, statuses, priorities, admin user)
        DataSeeder.seed();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/layout/Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 500, 600);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setTitle("Ticksy - Sign In");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Dark title bar on Windows
        DarkTitleBar.enable(primaryStage);
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
