package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.AppShell;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.View;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class InicioController {

    @FXML private Label lblBienvenida;
    @FXML private Label lblRol;
    @FXML private VBox cardUsuarios;

    @FXML private ImageView imgEquipo;
    @FXML private ImageView imgJugador;
    @FXML private ImageView imgPartido;
    @FXML private ImageView imgUsuario;

    @FXML
    public void initialize() {
        cargarBienvenida();
        configurarPermisos();
        cargarImagenes();
    }

    private void cargarImagenes() {
        cargarImagen(imgEquipo, "equipo.png");
        cargarImagen(imgJugador, "jugador-de-baloncesto.png");
        cargarImagen(imgPartido, "pista-de-baloncesto.png");
        cargarImagen(imgUsuario, "agregar-usuario.png");
    }

    private void cargarImagen(ImageView imageView, String nombreArchivo) {
        String[] rutas = {
                "/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/images/" + nombreArchivo,
                "/com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx/images/" + nombreArchivo,
                "/images/" + nombreArchivo
        };

        for (String ruta : rutas) {
            InputStream stream = getClass().getResourceAsStream(ruta);

            if (stream != null) {
                imageView.setImage(new Image(stream));
                return;
            }
        }

        System.err.println("No se encontro la imagen: " + nombreArchivo);
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