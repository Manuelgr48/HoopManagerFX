package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class DashboardController {

    @FXML private StackPane contentArea;

    @FXML private Button btnInicio;
    @FXML private Button btnEquipos;
    @FXML private Button btnJugadores;
    @FXML private Button btnPartidos;
    @FXML private Button btnRendimiento;
    @FXML private Button btnUsuarios;

    @FXML
    public void initialize() {
        contentArea.setId("contentArea");
        configurarPermisos();
        mostrarInicio();
    }

    private void configurarPermisos() {
        boolean esAdmin = SessionManager.getInstance().esAdmin();

        btnUsuarios.setVisible(esAdmin);
        btnUsuarios.setManaged(esAdmin);
    }

    @FXML
    private void mostrarInicio() {
        cargarVista("inicio-view.fxml");
    }

    @FXML
    private void mostrarEquipos() {
        cargarVista("equipos-view.fxml");
    }

    @FXML
    private void mostrarJugadores() {
        cargarVista("jugadores-view.fxml");
    }

    @FXML
    private void mostrarPartidos() {
        cargarVista("partidos-view.fxml");
    }

    @FXML
    private void mostrarRendimiento() {
        cargarVista("rendimiento-view.fxml");
    }

    @FXML
    private void mostrarUsuarios() {
        if (!SessionManager.getInstance().esAdmin()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acceso denegado", "Solo el administrador puede gestionar usuarios.");
            return;
        }

        cargarVista("usuarios-view.fxml");
    }

    private void cargarVista(String nombreFxml) {
        try {
            String ruta = "/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/" + nombreFxml;
            URL recurso = getClass().getResource(ruta);

            if (recurso == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Vista no encontrada", "No se encontro el archivo: " + nombreFxml);
                return;
            }

            Parent vista = FXMLLoader.load(recurso);
            contentArea.getChildren().setAll(vista);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista: " + nombreFxml);
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}