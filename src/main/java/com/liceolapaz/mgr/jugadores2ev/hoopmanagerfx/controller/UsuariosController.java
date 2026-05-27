package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
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
    @FXML private TableColumn<Usuario, String> colNombreEquipo;

    @FXML private ComboBox<String> cbRol;
    @FXML private TextField txtNombreEquipo;
    @FXML private Label lblMensaje;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private EquipoDAO equipoDAO = new EquipoDAO();
    private ObservableList<Usuario> listaUsuarios;

    @FXML
    public void initialize() {
        listaUsuarios = FXCollections.observableArrayList();

        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colNombreEquipo.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));

        cargarUsuarios();

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cbRol.setValue(newSelection.getRol());
                txtNombreEquipo.setText(newSelection.getNombreEquipo().equals("Sin asignar") ? "" : newSelection.getNombreEquipo());
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
        String nombreIntroducido = txtNombreEquipo.getText().trim();

        if (!nombreIntroducido.isEmpty()) {
            nuevoIdEquipo = equipoDAO.obtenerIdPorNombre(nombreIntroducido);
            if (nuevoIdEquipo == null) {
                mostrarMensaje("El equipo '" + nombreIntroducido + "' no existe. Escribe un nombre correcto.", true);
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
            txtNombreEquipo.clear();
            cbRol.setValue(null);
        } else {
            mostrarMensaje("Error al eliminar el usuario.", true);
        }
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle(esError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #2ecc71;");
    }
}