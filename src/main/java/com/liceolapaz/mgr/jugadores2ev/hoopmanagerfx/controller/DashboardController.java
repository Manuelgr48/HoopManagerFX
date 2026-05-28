package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.AppShell;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DashboardController {

    @FXML private StackPane contentArea;
    @FXML private HBox breadcrumbBar;

    @FXML
    public void initialize() {
        AppShell.setShell(contentArea, breadcrumbBar);
        AppShell.loadView(View.INICIO);
    }

    @FXML
    private void mostrarInicio() {
        AppShell.loadView(View.INICIO);
    }

    @FXML
    private void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar sesion");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("Seguro que quieres cerrar sesion?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
            return;
        }

        SessionManager.getInstance().cerrarSesion();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/login-view.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();

            stage.getScene().setRoot(root);
            stage.setTitle("HoopManagerFX - Login");
            stage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo volver al login.");
            alerta.showAndWait();
        }
    }
}