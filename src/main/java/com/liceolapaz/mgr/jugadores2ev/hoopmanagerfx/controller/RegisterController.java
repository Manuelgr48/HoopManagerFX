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

public class RegisterController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void onRegisterClick(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String correo = txtCorreo.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            mostrarError("Rellena todos los campos.");
            return;
        }

        if (!contieneSoloLetras(nombre) || !contieneSoloLetras(apellidos)) {
            mostrarError("El nombre y apellidos solo pueden contener letras.");
            return;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            mostrarError("Introduce un correo electronico valido.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarError("Las contrasenas no coinciden.");
            return;
        }

        if (password.length() < 6) {
            mostrarError("La contrasena debe tener minimo 6 caracteres.");
            return;
        }

        boolean hasUpper = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        if (!hasUpper || !hasDigit) {
            mostrarError("La contrasena debe tener al menos una mayuscula y un numero.");
            return;
        }

        boolean exito = usuarioDAO.registrarUsuario(nombre, apellidos, correo, password, "JUGADOR", null);

        if (!exito) {
            mostrarError("Error. Es posible que el correo ya este registrado.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticarUsuario(correo, password);

        if (usuario == null) {
            mostrarError("Cuenta creada, pero no se pudo iniciar sesion automaticamente.");
            return;
        }

        SessionManager.getInstance().iniciarSesion(usuario.getCorreo(), usuario.getRol(), usuario.getIdEquipo());
        abrirDashboard(event);
    }

    private boolean contieneSoloLetras(String texto) {
        for (char c : texto.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                return false;
            }
        }

        return true;
    }

    private void abrirDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/dashboard-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("HoopManagerFX - Panel Principal");
            stage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al abrir el panel principal.");
        }
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setStyle("-fx-text-fill: #e74c3c;");
        lblMensaje.setText(mensaje);
    }

    @FXML
    public void volverAlLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("HoopManagerFX - Login");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}