package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    @FXML
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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/dashboard-view.fxml"));
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(loader.load(), 900, 600));
                stage.setTitle("HoopManagerFX - Panel Principal");
                stage.centerOnScreen();
            } catch (IOException e) {
                lblMensaje.setStyle("-fx-text-fill: red;");
                lblMensaje.setText("Error al cargar la interfaz principal.");
                e.printStackTrace();
            }
        } else {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Usuario o contraseña incorrectos.");
        }
    }
}