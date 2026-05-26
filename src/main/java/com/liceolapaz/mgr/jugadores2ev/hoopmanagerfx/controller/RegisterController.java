package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void onRegisterClick() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Rellena todos los campos.");
            return;
        }
        boolean exito = usuarioDAO.registrarUsuario(username, password, "JUGADOR", null);

        if (exito) {
            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setText("Usuario registrado con éxito. Ya puedes iniciar sesión.");
            txtUsername.clear();
            txtPassword.clear();
        } else {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Error. El nombre de usuario ya existe.");
        }
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