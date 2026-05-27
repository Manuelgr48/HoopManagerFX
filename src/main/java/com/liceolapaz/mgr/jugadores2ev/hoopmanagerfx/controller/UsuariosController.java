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
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellidos;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colNombreEquipo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cbRol;
    @FXML private TextField txtNombreEquipo;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EquipoDAO equipoDAO = new EquipoDAO();
    private ObservableList<Usuario> listaUsuarios;

    @FXML
    public void initialize() {
        listaUsuarios = FXCollections.observableArrayList();

        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colNombreEquipo.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));

        cbRol.setItems(FXCollections.observableArrayList("JUGADOR", "ENTRENADOR", "ADMIN"));

        cargarUsuarios();

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, usuario) -> {
            if (usuario != null) {
                txtNombre.setText(usuario.getNombre());
                txtApellidos.setText(usuario.getApellidos());
                txtCorreo.setText(usuario.getCorreo());
                txtPassword.clear();
                cbRol.setValue(usuario.getRol());
                txtNombreEquipo.setText(usuario.getNombreEquipo().equals("Sin asignar") ? "" : usuario.getNombreEquipo());
            }
        });
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        listaUsuarios.addAll(usuarioDAO.getAllUsuarios());
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    private void crearUsuario() {
        if (!validarCampos(true)) {
            return;
        }

        Integer idEquipo = obtenerIdEquipoDesdeTexto();
        if (idEquipo == null && !txtNombreEquipo.getText().trim().isEmpty()) {
            return;
        }

        boolean exito = usuarioDAO.registrarUsuario(
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                txtCorreo.getText().trim(),
                txtPassword.getText(),
                cbRol.getValue(),
                idEquipo
        );

        if (exito) {
            mostrarMensaje("Usuario creado correctamente.", false);
            cargarUsuarios();
            limpiarFormulario();
        } else {
            mostrarMensaje("No se pudo crear el usuario. Revisa si el correo ya existe.", true);
        }
    }

    @FXML
    private void actualizarUsuario() {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            mostrarMensaje("Selecciona un usuario de la tabla primero.", true);
            return;
        }

        if (!validarCampos(false)) {
            return;
        }

        Integer idEquipo = obtenerIdEquipoDesdeTexto();
        if (idEquipo == null && !txtNombreEquipo.getText().trim().isEmpty()) {
            return;
        }

        boolean exito = usuarioDAO.actualizarUsuario(
                usuarioSeleccionado.getIdUsuario(),
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                txtCorreo.getText().trim(),
                txtPassword.getText(),
                cbRol.getValue(),
                idEquipo
        );

        if (exito) {
            mostrarMensaje("Usuario actualizado correctamente.", false);
            cargarUsuarios();
            limpiarFormulario();
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
            mostrarMensaje("No puedes borrar a un administrador desde aqui.", true);
            return;
        }

        boolean exito = usuarioDAO.eliminarUsuario(usuarioSeleccionado.getIdUsuario());

        if (exito) {
            mostrarMensaje("Usuario eliminado correctamente.", false);
            cargarUsuarios();
            limpiarFormulario();
        } else {
            mostrarMensaje("Error al eliminar el usuario.", true);
        }
    }

    @FXML
    private void limpiarFormulario() {
        tablaUsuarios.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtApellidos.clear();
        txtCorreo.clear();
        txtPassword.clear();
        cbRol.setValue(null);
        txtNombreEquipo.clear();
    }

    private boolean validarCampos(boolean passwordObligatoria) {
        if (txtNombre.getText().trim().isEmpty()
                || txtApellidos.getText().trim().isEmpty()
                || txtCorreo.getText().trim().isEmpty()
                || cbRol.getValue() == null) {
            mostrarMensaje("Rellena nombre, apellidos, correo y rol.", true);
            return false;
        }

        if (!txtCorreo.getText().contains("@") || !txtCorreo.getText().contains(".")) {
            mostrarMensaje("Introduce un correo valido.", true);
            return false;
        }

        if (passwordObligatoria && txtPassword.getText().trim().isEmpty()) {
            mostrarMensaje("La contrasena es obligatoria al crear usuarios.", true);
            return false;
        }

        if (!txtPassword.getText().trim().isEmpty() && txtPassword.getText().length() < 4) {
            mostrarMensaje("La contrasena debe tener al menos 4 caracteres.", true);
            return false;
        }

        return true;
    }

    private Integer obtenerIdEquipoDesdeTexto() {
        String nombreEquipo = txtNombreEquipo.getText().trim();

        if (nombreEquipo.isEmpty()) {
            return null;
        }

        Integer idEquipo = equipoDAO.obtenerIdPorNombre(nombreEquipo);

        if (idEquipo == null) {
            mostrarMensaje("El equipo '" + nombreEquipo + "' no existe.", true);
        }

        return idEquipo;
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle(esError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #2ecc71;");
    }
}