package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 600, 400));
            stage.setTitle("HoopManagerFX - Acceso");
            stage.centerOnScreen();
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
        }
    }
}