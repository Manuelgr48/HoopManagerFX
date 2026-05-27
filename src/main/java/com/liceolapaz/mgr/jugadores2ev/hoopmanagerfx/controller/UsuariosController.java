package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.UsuarioDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Usuario;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class UsuariosController {

    @FXML private TextField tfBuscador;

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
    @FXML private ComboBox<Equipo> cbEquipo;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EquipoDAO equipoDAO = new EquipoDAO();

    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private final ObservableList<Equipo> listaEquipos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (!SessionManager.getInstance().esAdmin()) {
            mostrarMensaje("Acceso denegado. Solo el administrador puede gestionar usuarios.", true);
            return;
        }

        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colNombreEquipo.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));

        tablaUsuarios.setItems(listaUsuarios);

        cbRol.setItems(FXCollections.observableArrayList("JUGADOR", "ENTRENADOR", "ADMIN"));
        cbEquipo.setItems(listaEquipos);

        tfBuscador.textProperty().addListener((obs, oldText, newText) -> buscarUsuarios());

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, usuario) -> {
            if (usuario != null) {
                rellenarFormulario(usuario);
            }
        });

        cargarEquipos();
        cargarUsuarios();
    }

    private void cargarEquipos() {
        listaEquipos.clear();
        listaEquipos.addAll(equipoDAO.obtenerTodos());
    }

    private void cargarUsuarios() {
        listaUsuarios.clear();
        listaUsuarios.addAll(usuarioDAO.getAllUsuarios());
    }

    @FXML
    private void buscarUsuarios() {
        String filtro = tfBuscador.getText();

        listaUsuarios.clear();

        if (filtro == null || filtro.trim().isEmpty()) {
            listaUsuarios.addAll(usuarioDAO.getAllUsuarios());
        } else {
            listaUsuarios.addAll(usuarioDAO.buscar(filtro.trim()));
        }
    }

    private void rellenarFormulario(Usuario usuario) {
        txtNombre.setText(usuario.getNombre());
        txtApellidos.setText(usuario.getApellidos());
        txtCorreo.setText(usuario.getCorreo());
        txtPassword.clear();
        cbRol.setValue(usuario.getRol());
        cbEquipo.setValue(buscarEquipoPorId(usuario.getIdEquipo()));
    }

    @FXML
    private void crearUsuario() {
        if (!validarCampos(true)) {
            return;
        }

        Integer idEquipo = cbEquipo.getValue() != null ? cbEquipo.getValue().getIdEquipo() : null;

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
            buscarUsuarios();
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

        if (usuarioDAO.esUltimoAdmin(usuarioSeleccionado.getIdUsuario()) && !"ADMIN".equals(cbRol.getValue())) {
            mostrarMensaje("No puedes quitar el rol ADMIN al ultimo administrador.", true);
            return;
        }

        if (!confirmar("Confirmar modificacion", "Seguro que quieres modificar este usuario?")) {
            return;
        }

        Integer idEquipo = cbEquipo.getValue() != null ? cbEquipo.getValue().getIdEquipo() : null;

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
            buscarUsuarios();
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

        if (usuarioDAO.esUltimoAdmin(usuarioSeleccionado.getIdUsuario())) {
            mostrarMensaje("No puedes borrar al ultimo administrador.", true);
            return;
        }

        if (!confirmar("Confirmar eliminacion", "Seguro que quieres eliminar este usuario?")) {
            return;
        }

        boolean exito = usuarioDAO.eliminarUsuario(usuarioSeleccionado.getIdUsuario());

        if (exito) {
            mostrarMensaje("Usuario eliminado correctamente.", false);
            buscarUsuarios();
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
        cbEquipo.setValue(null);
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

    private Equipo buscarEquipoPorId(Integer idEquipo) {
        if (idEquipo == null) {
            return null;
        }

        for (Equipo equipo : listaEquipos) {
            if (equipo.getIdEquipo() == idEquipo) {
                return equipo;
            }
        }

        return null;
    }

    private boolean confirmar(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle(esError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #2ecc71;");
    }
}