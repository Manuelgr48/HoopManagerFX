package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.AppShell;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private VBox centerContainer;

    @FXML
    public void initialize() {
        AppShell.setCenterContainer(centerContainer);
    }

    @FXML
    public void mostrarEquipos(ActionEvent event) {
        System.out.println("Navegando a Equipos a traves de AppShell...");
        AppShell.loadView(View.EQUIPOS);
    }

    @FXML
    public void mostrarJugadores(ActionEvent event) {
        System.out.println("Navegando a Jugadores a traves de AppShell...");
        AppShell.loadView(View.JUGADORES);
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().cerrarSesion();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 600, 400));
            stage.setTitle("HoopManagerFX - Acceso");
            stage.centerOnScreen();
        } catch (IOException e) {
            System.err.println("Error al redirigir al login: " + e.getMessage());
        }
    }
}