package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AppShell {
    private static VBox centerContainer;

    public static void setCenterContainer(VBox container) {
        centerContainer = container;
    }

    public static void loadView(View view) {
        if (centerContainer == null) {
            System.err.println("Error: El contenedor central de AppShell no ha sido inicializado.");
            return;
        }

        try {
            centerContainer.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(AppShell.class.getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/" + view.getFxmlFile()));
            Parent node = loader.load();
            centerContainer.getChildren().add(node);
        } catch (IOException e) {
            System.err.println("Error al cargar la subvista de AppShell: " + e.getMessage());
            e.printStackTrace();
        }
    }
}