package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    @FXML
    public void handleLogin(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Por favor, rellena todos los campos.");
            return;
        }

        if ("admin".equals(usuario) && "1234".equals(password)) {
            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setText("¡Login correcto! Accediendo a HoopManager...");
        } else {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Usuario o contraseña incorrectos.");
        }
    }
}