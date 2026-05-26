package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("HoopManagerFX - Acceso");
        stage.setScene(scene);
        stage.setMaximized(true);
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/favicon_basketball.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el icono: " + e.getMessage());
        }

        stage.show();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}