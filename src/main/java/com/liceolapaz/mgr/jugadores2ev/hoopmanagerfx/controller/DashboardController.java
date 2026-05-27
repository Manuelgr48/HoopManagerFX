package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.AppShell;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.View;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML private StackPane contentArea;
    @FXML private Label lblBreadcrumb;

    @FXML
    public void initialize() {
        AppShell.setShell(contentArea, lblBreadcrumb);
        AppShell.loadView(View.INICIO);
    }

    @FXML
    private void mostrarInicio() {
        AppShell.loadView(View.INICIO);
    }
}