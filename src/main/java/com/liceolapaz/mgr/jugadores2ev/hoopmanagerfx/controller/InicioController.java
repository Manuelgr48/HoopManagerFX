package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.AppShell;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.View;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InicioController {

    @FXML private Label lblBienvenida;
    @FXML private Label lblRol;
    @FXML private VBox cardUsuarios;

    @FXML
    public void initialize() {
        cargarBienvenida();
        configurarPermisos();
    }

    private void cargarBienvenida() {
        SessionManager session = SessionManager.getInstance();

        String usuario = session.getUsuarioLogueado();
        String rol = session.getRol();

        if (usuario == null || usuario.isBlank()) {
            lblBienvenida.setText("HoopManager");
            lblRol.setText("Sesion no identificada");
            return;
        }

        lblBienvenida.setText("HoopManager");
        lblRol.setText("Sesion iniciada como " + usuario + " | " + rol);
    }

    private void configurarPermisos() {
        boolean esAdmin = SessionManager.getInstance().esAdmin();

        cardUsuarios.setVisible(esAdmin);
        cardUsuarios.setManaged(esAdmin);
    }

    @FXML
    private void irEquipos() {
        AppShell.loadView(View.EQUIPOS);
    }

    @FXML
    private void irJugadores() {
        AppShell.loadView(View.JUGADORES);
    }

    @FXML
    private void irPartidos() {
        AppShell.loadView(View.PARTIDOS);
    }

    @FXML
    private void irUsuarios() {
        AppShell.loadView(View.USUARIOS);
    }
}