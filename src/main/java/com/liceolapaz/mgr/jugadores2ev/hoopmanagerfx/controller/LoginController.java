package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.UsuarioDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Usuario;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void handleLogin(ActionEvent event) {
        String correoInput = txtCorreo.getText().trim();
        String passwordInput = txtPassword.getText();

        if (correoInput.isEmpty() || passwordInput.isEmpty()) {
            mostrarError("Por favor, rellena todos los campos.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticarUsuario(correoInput, passwordInput);

        if (usuario != null) {
            SessionManager.getInstance().iniciarSesion(usuario.getCorreo(), usuario.getRol(), usuario.getIdEquipo());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/dashboard-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.getScene().setRoot(root);
                stage.setTitle("HoopManagerFX - Panel Principal");

                stage.setMaximized(true);

            } catch (IOException e) {
                mostrarError("Error al cargar la interfaz principal.");
                e.printStackTrace();
            }
        } else {
            mostrarError("Correo o contraseña incorrectos.");
        }
    }

    @FXML
    public void abrirRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/register-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.getScene().setRoot(root);
            stage.setTitle("HoopManagerFX - Registro");

        } catch (IOException e) {
            mostrarError("Error al abrir la ventana de registro.");
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setStyle("-fx-text-fill: #e74c3c;");
        lblMensaje.setText(mensaje);
    }
}