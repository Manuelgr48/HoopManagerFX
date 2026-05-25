package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private StackPane contentArea;


    @FXML
    public void initialize() {
        mostrarResumen();
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