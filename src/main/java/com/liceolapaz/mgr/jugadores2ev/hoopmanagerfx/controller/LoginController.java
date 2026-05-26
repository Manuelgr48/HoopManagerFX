package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.UsuarioDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Usuario;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void handleLogin(ActionEvent event) {
        String usuarioInput = txtUsuario.getText();
        String passwordInput = txtPassword.getText();

        if (usuarioInput.isEmpty() || passwordInput.isEmpty()) {
            mostrarError("Por favor, rellena todos los campos.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticarUsuario(usuarioInput, passwordInput);

        if (usuario != null) {
            SessionManager.getInstance().iniciarSesion(usuario.getUsername(), usuario.getRol(), usuario.getIdEquipo());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/dashboard-view.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(loader.load(), 900, 600));
                stage.setTitle("HoopManagerFX - Panel Principal");
                stage.centerOnScreen();
            } catch (IOException e) {
                mostrarError("Error al cargar la interfaz principal.");
                e.printStackTrace();
            }
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
        }
    }
    @FXML
    public void abrirRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/dashboard-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("HoopManagerFX - Panel Principal");
            stage.setResizable(true);

            stage.setMaximized(true);
            javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

        } catch (IOException e) {
            mostrarError("Error al cargar la interfaz principal.");
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setStyle("-fx-text-fill: red;");
        lblMensaje.setText(mensaje);
    }
}