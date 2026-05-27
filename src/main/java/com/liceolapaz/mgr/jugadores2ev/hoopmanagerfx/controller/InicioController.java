package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InicioController {

    @FXML private Label lblBienvenida;
    @FXML private Label lblRol;
    @FXML private Label lblTotalEquipos;
    @FXML private Label lblTotalJugadores;
    @FXML private Label lblTotalPartidos;
    @FXML private Label lblSugerencia;

    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final PartidoService partidoService = new PartidoService();

    @FXML
    public void initialize() {
        cargarBienvenida();
        cargarResumen();
        cargarSugerencia();
    }

    private void cargarBienvenida() {
        SessionManager session = SessionManager.getInstance();

        String usuario = session.getUsuarioLogueado();
        String rol = session.getRol();

        if (usuario == null || usuario.isBlank()) {
            lblBienvenida.setText("Bienvenido a HoopManager");
            lblRol.setText("Sesion no identificada");
            return;
        }

        lblBienvenida.setText("Bienvenido, " + usuario);
        lblRol.setText("Rol actual: " + rol);
    }

    private void cargarResumen() {
        try {
            lblTotalEquipos.setText(String.valueOf(equipoDAO.obtenerTodos().size()));
            lblTotalJugadores.setText(String.valueOf(jugadorDAO.obtenerTodos().size()));
            lblTotalPartidos.setText(String.valueOf(partidoService.obtenerTodosLosPartidos().size()));
        } catch (Exception e) {
            e.printStackTrace();
            lblTotalEquipos.setText("-");
            lblTotalJugadores.setText("-");
            lblTotalPartidos.setText("-");
        }
    }

    private void cargarSugerencia() {
        SessionManager session = SessionManager.getInstance();

        if (session.esAdmin()) {
            lblSugerencia.setText("Puedes gestionar usuarios, equipos, jugadores, partidos y consultar el rendimiento general.");
        } else if (session.esEntrenador()) {
            lblSugerencia.setText("Accede a Equipos, entra en tu equipo asignado y gestiona sus jugadores y estadisticas.");
        } else {
            lblSugerencia.setText("Consulta equipos, jugadores, partidos y estadisticas desde las vistas principales.");
        }
    }
}