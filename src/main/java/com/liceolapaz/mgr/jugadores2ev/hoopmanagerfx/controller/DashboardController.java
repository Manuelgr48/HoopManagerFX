package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private StackPane contentArea;
    @FXML private Button btnUsuarios;

    @FXML
    public void initialize() {
        mostrarResumen();

        String rol = SessionManager.getInstance().getRol();
        if (!"ADMIN".equals(rol)) {
            btnUsuarios.setVisible(false);
            btnUsuarios.setManaged(false);
        }
    }

    @FXML
    private void mostrarResumen() {
        cargarVista("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/resumen-view.fxml");
    }

    @FXML
    private void mostrarEquipos() {
        cargarVista("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/equipos-view.fxml");
    }

    @FXML
    private void mostrarJugadores() {
        cargarVista("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/jugadores-view.fxml");
    }

    @FXML
    private void mostrarPartidos() {
        cargarVista("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/partidos-view.fxml");
    }

    @FXML
    private void mostrarEstadisticas() {
        cargarVista("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/estadistica-view.fxml");
    }

    // Nuevo método para cargar la vista de usuarios
    @FXML
    private void mostrarUsuarios() {
        cargarVista("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/usuarios-view.fxml");
    }

    private void cargarVista(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error cargando la vista: " + fxmlPath);
        }
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        SessionManager.getInstance().cerrarSesion();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}