package com.ticksy.util;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class ViewNavigator {

    private static StackPane contentArea;

    public static void setContentArea(StackPane area) {
        contentArea = area;
    }

    public static void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewNavigator.class.getResource(fxmlPath));
            Parent view = loader.load();
            animateSwap(view);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }

    public static <T> T loadViewAndGetController(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewNavigator.class.getResource(fxmlPath));
            Parent view = loader.load();
            animateSwap(view);
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }

    private static void animateSwap(Parent newView) {
        newView.setOpacity(0);
        newView.setTranslateY(12);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(newView);

        FadeTransition fade = new FadeTransition(Duration.millis(250), newView);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(250), newView);
        slide.setFromY(12);
        slide.setToY(0);

        fade.play();
        slide.play();
    }
}
