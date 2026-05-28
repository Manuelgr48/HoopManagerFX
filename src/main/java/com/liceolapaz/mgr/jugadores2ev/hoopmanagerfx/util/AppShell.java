package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class AppShell {

    private static StackPane contentArea;
    private static HBox breadcrumbBar;

    private AppShell() {
    }

    public static void setShell(StackPane container, HBox breadcrumbs) {
        contentArea = container;
        breadcrumbBar = breadcrumbs;
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
        if (breadcrumbBar == null) {
            return;
        }

        breadcrumbBar.getChildren().clear();

        breadcrumbBar.getChildren().add(crearMiga("Inicio", View.INICIO));

        if (view != View.INICIO) {
            Label separador = new Label("/");
            separador.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 14px;");
            breadcrumbBar.getChildren().add(separador);
            breadcrumbBar.getChildren().add(crearMiga(view.getTitulo(), view));
        }
    }

    private static Button crearMiga(String texto, View view) {
        Button boton = new Button(texto);
        boton.setOnAction(event -> loadView(view));
        boton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2563eb; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        return boton;
    }

    private static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}