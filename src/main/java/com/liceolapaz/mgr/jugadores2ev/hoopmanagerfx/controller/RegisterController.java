package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.UsuarioDAO;
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

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void onRegisterClick() {
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
            mostrarError("Introduce un correo electrónico válido.");
            return;
        }


        if (!password.equals(confirmPassword)) {
            mostrarError("Las contraseñas no coinciden.");
            return;
        }

        if (password.length() < 6) {
            mostrarError("La contraseña debe tener mín. 6 caracteres.");
            return;
        }
        boolean hasUpper = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        if (!hasUpper || !hasDigit) {
            mostrarError("La contraseña debe tener al menos una mayúscula y un número.");
            return;
        }

        boolean exito = usuarioDAO.registrarUsuario(nombre, apellidos, correo, password, "JUGADOR", null);

        if (exito) {
            lblMensaje.setStyle("-fx-text-fill: #2ecc71;");
            lblMensaje.setText("¡Cuenta creada! Ya puedes iniciar sesión.");
            txtNombre.clear();
            txtApellidos.clear();
            txtCorreo.clear();
            txtPassword.clear();
            txtConfirmPassword.clear();
        } else {
            mostrarError("Error. Es posible que el correo ya esté registrado.");
        }
    }

    private boolean contieneSoloLetras(String texto) {
        for (char c : texto.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
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