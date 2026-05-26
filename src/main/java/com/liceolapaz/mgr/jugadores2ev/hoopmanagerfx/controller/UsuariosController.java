package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.UsuarioDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colIdUsuario;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Integer> colIdEquipo;

    @FXML private ComboBox<String> cbRol;
    @FXML private TextField txtIdEquipo;
    @FXML private Label lblMensaje;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ObservableList<Usuario> listaUsuarios;

    @FXML
    public void initialize() {
        listaUsuarios = FXCollections.observableArrayList();

        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colIdEquipo.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));

        cargarUsuarios();

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cbRol.setValue(newSelection.getRol());
                if (newSelection.getIdEquipo() != null) {
                    txtIdEquipo.setText(String.valueOf(newSelection.getIdEquipo()));
                } else {
                    txtIdEquipo.setText("");
                }
            }
        });
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        listaUsuarios.addAll(usuarioDAO.getAllUsuarios());
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    private void actualizarUsuario() {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Selecciona un usuario de la tabla primero.", true);
            return;
        }

        String nuevoRol = cbRol.getValue();
        Integer nuevoIdEquipo = null;

        if (!txtIdEquipo.getText().trim().isEmpty()) {
            try {
                nuevoIdEquipo = Integer.parseInt(txtIdEquipo.getText().trim());
            } catch (NumberFormatException e) {
                mostrarMensaje("El ID de equipo debe ser un número.", true);
                return;
            }
        }

        boolean exito = usuarioDAO.actualizarRolYEquipo(usuarioSeleccionado.getIdUsuario(), nuevoRol, nuevoIdEquipo);
        if (exito) {
            mostrarMensaje("Usuario actualizado correctamente.", false);
            cargarUsuarios();
        } else {
            mostrarMensaje("Error al actualizar el usuario.", true);
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Selecciona un usuario de la tabla primero.", true);
            return;
        }

        if ("ADMIN".equals(usuarioSeleccionado.getRol())) {
            mostrarMensaje("No puedes borrar a un Administrador desde aquí.", true);
            return;
        }

        boolean exito = usuarioDAO.eliminarUsuario(usuarioSeleccionado.getIdUsuario());
        if (exito) {
            mostrarMensaje("Usuario eliminado correctamente.", false);
            cargarUsuarios();
            txtIdEquipo.clear();
            cbRol.setValue(null);
        } else {
            mostrarMensaje("Error al eliminar el usuario.", true);
        }
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle(esError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}