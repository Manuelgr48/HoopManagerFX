package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class AppShell {

    private static StackPane contentArea;
    private static Label breadcrumbLabel;

    private AppShell() {
    }

    public static void setShell(StackPane container, Label breadcrumb) {
        contentArea = container;
        breadcrumbLabel = breadcrumb;
        contentArea.setId("contentArea");
    }

    public static void loadView(View view) {
        if (view.isSoloAdmin() && !SessionManager.getInstance().esAdmin()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acceso denegado", "Solo el administrador puede acceder a esta zona.");
            return;
        }

        if (contentArea == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se ha inicializado la zona principal.");
            return;
        }

        try {
            URL recurso = AppShell.class.getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/" + view.getFxmlFile());

            if (recurso == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Vista no encontrada", "No se encontro: " + view.getFxmlFile());
                return;
            }

            Parent vista = FXMLLoader.load(recurso);
            contentArea.getChildren().setAll(vista);
            actualizarBreadcrumb(view);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista: " + view.getFxmlFile());
        }
    }

    private static void actualizarBreadcrumb(View view) {
        if (breadcrumbLabel == null) {
            return;
        }

        if (view == View.INICIO) {
            breadcrumbLabel.setText("Inicio");
        } else {
            breadcrumbLabel.setText("Inicio / " + view.getTitulo());
        }
    }

    private static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}