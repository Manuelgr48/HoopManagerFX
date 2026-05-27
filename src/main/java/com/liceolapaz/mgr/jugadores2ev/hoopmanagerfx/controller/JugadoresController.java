package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class JugadoresController implements Initializable {

    @FXML private Label lblTitulo;

    @FXML private TableView<Jugador> tablaJugadores;
    @FXML private TableColumn<Jugador, Integer> colId;
    @FXML private TableColumn<Jugador, String> colNombre;
    @FXML private TableColumn<Jugador, String> colApellidos;
    @FXML private TableColumn<Jugador, Integer> colDorsal;
    @FXML private TableColumn<Jugador, String> colPosicion;
    @FXML private TableColumn<Jugador, Double> colAltura;
    @FXML private TableColumn<Jugador, String> colEquipo;

    @FXML private TextField tfBuscador;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDorsal;
    @FXML private TextField txtPosicion;
    @FXML private TextField txtAltura;
    @FXML private ComboBox<Equipo> cbEquipo;

    @FXML private GridPane formularioJugador;
    @FXML private HBox botonesCrud;

    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final EquipoDAO equipoDAO = new EquipoDAO();

    private final ObservableList<Jugador> listaJugadores = FXCollections.observableArrayList();
    private final ObservableList<Equipo> listaEquipos = FXCollections.observableArrayList();

    private FilteredList<Jugador> filteredData;
    private Integer idEquipoForzado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDorsal.setCellValueFactory(new PropertyValueFactory<>("dorsal"));
        colPosicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        colEquipo.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));

        cbEquipo.setItems(listaEquipos);

        cargarEquipos();
        configurarPermisos();
        configurarBuscador();
        cargarJugadores();

        tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, jugador) -> {
            if (jugador != null) {
                rellenarFormulario(jugador);
            }
        });
    }

    private void cargarEquipos() {
        listaEquipos.clear();
        listaEquipos.addAll(equipoDAO.obtenerTodos());
    }

    private void configurarPermisos() {
        SessionManager sesion = SessionManager.getInstance();

        if (sesion.esAdmin()) {
            lblTitulo.setText("Gestion de Jugadores");
            mostrarCrud(true);
            cbEquipo.setDisable(false);
            idEquipoForzado = null;
            return;
        }

        if (sesion.esEntrenador()) {
            if (sesion.getIdEquipo() == null) {
                lblTitulo.setText("Entrenador sin equipo asignado");
                mostrarCrud(false);
                idEquipoForzado = -1;
                return;
            }

            lblTitulo.setText("Mi equipo");
            mostrarCrud(true);
            cbEquipo.setDisable(true);
            idEquipoForzado = sesion.getIdEquipo();
            cbEquipo.setValue(buscarEquipoPorId(idEquipoForzado));
            return;
        }

        lblTitulo.setText("Jugadores");
        mostrarCrud(false);
        idEquipoForzado = null;
    }

    private void mostrarCrud(boolean visible) {
        formularioJugador.setVisible(visible);
        formularioJugador.setManaged(visible);
        botonesCrud.setVisible(visible);
        botonesCrud.setManaged(visible);
    }

    private void configurarBuscador() {
        filteredData = new FilteredList<>(listaJugadores, jugador -> true);

        tfBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(jugador -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String filtro = newValue.toLowerCase();

                return jugador.getNombre().toLowerCase().contains(filtro)
                        || jugador.getApellidos().toLowerCase().contains(filtro)
                        || jugador.getNombreEquipo().toLowerCase().contains(filtro);
            });
        });

        SortedList<Jugador> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaJugadores.comparatorProperty());
        tablaJugadores.setItems(sortedData);
    }

    private void cargarJugadores() {
        listaJugadores.clear();

        if (idEquipoForzado != null) {
            listaJugadores.addAll(jugadorDAO.obtenerPorEquipo(idEquipoForzado));
        } else {
            listaJugadores.addAll(jugadorDAO.obtenerTodos());
        }
    }

    private void rellenarFormulario(Jugador jugador) {
        txtNombre.setText(jugador.getNombre());
        txtApellidos.setText(jugador.getApellidos());
        txtDorsal.setText(String.valueOf(jugador.getDorsal()));
        txtPosicion.setText(jugador.getPosicion());
        txtAltura.setText(String.valueOf(jugador.getAltura()));
        cbEquipo.setValue(buscarEquipoPorId(jugador.getIdEquipo()));
    }

    @FXML
    private void anadirJugador() {
        if (!validarCampos()) return;

        Integer idEquipo = obtenerIdEquipoParaGuardar();
        int dorsal = Integer.parseInt(txtDorsal.getText().trim());

        if (!validarDorsalDisponible(idEquipo, dorsal, null)) return;

        Jugador nuevoJugador = new Jugador(
                0,
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                dorsal,
                txtPosicion.getText().trim(),
                Double.parseDouble(txtAltura.getText().trim()),
                idEquipo,
                null
        );

        if (jugadorDAO.insertar(nuevoJugador)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Jugador anadido correctamente.");
            cargarJugadores();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo anadir el jugador.");
        }
    }

    @FXML
    private void modificarJugador() {
        Jugador seleccionado = tablaJugadores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona un jugador para modificar.");
            return;
        }

        if (!puedeGestionarJugador(seleccionado)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Permiso denegado", "No puedes modificar jugadores de otro equipo.");
            return;
        }

        if (!validarCampos()) return;

        if (!confirmar("Confirmar modificacion", "Seguro que quieres modificar este jugador?")) {
            return;
        }

        Integer idEquipo = obtenerIdEquipoParaGuardar();
        int dorsal = Integer.parseInt(txtDorsal.getText().trim());

        if (!validarDorsalDisponible(idEquipo, dorsal, seleccionado.getIdJugador())) return;

        Jugador jugadorModificado = new Jugador(
                seleccionado.getIdJugador(),
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                dorsal,
                txtPosicion.getText().trim(),
                Double.parseDouble(txtAltura.getText().trim()),
                idEquipo,
                null
        );

        if (jugadorDAO.actualizar(jugadorModificado)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Jugador modificado correctamente.");
            cargarJugadores();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar el jugador.");
        }
    }

    @FXML
    private void eliminarJugador() {
        Jugador seleccionado = tablaJugadores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona un jugador para eliminar.");
            return;
        }

        if (!puedeGestionarJugador(seleccionado)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Permiso denegado", "No puedes eliminar jugadores de otro equipo.");
            return;
        }

        if (!confirmar("Confirmar eliminacion", "Seguro que quieres eliminar a este jugador?")) {
            return;
        }

        if (jugadorDAO.eliminar(seleccionado.getIdJugador())) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Jugador eliminado correctamente.");
            cargarJugadores();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el jugador.");
        }
    }

    @FXML
    private void limpiarCampos() {
        tablaJugadores.getSelectionModel().clearSelection();

        txtNombre.clear();
        txtApellidos.clear();
        txtDorsal.clear();
        txtPosicion.clear();
        txtAltura.clear();

        if (idEquipoForzado == null) {
            cbEquipo.setValue(null);
        } else {
            cbEquipo.setValue(buscarEquipoPorId(idEquipoForzado));
        }
    }

    private boolean puedeGestionarJugador(Jugador jugador) {
        SessionManager sesion = SessionManager.getInstance();

        if (sesion.esAdmin()) {
            return true;
        }

        return sesion.esEntrenador()
                && sesion.getIdEquipo() != null
                && jugador.getIdEquipo() != null
                && sesion.getIdEquipo().equals(jugador.getIdEquipo());
    }

    private Integer obtenerIdEquipoParaGuardar() {
        if (idEquipoForzado != null && idEquipoForzado > 0) {
            return idEquipoForzado;
        }

        Equipo equipo = cbEquipo.getValue();
        return equipo != null ? equipo.getIdEquipo() : null;
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()
                || txtApellidos.getText().trim().isEmpty()
                || txtDorsal.getText().trim().isEmpty()
                || txtPosicion.getText().trim().isEmpty()
                || txtAltura.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Rellena todos los campos obligatorios.");
            return false;
        }

        try {
            int dorsal = Integer.parseInt(txtDorsal.getText().trim());
            double altura = Double.parseDouble(txtAltura.getText().trim());

            if (dorsal <= 0) {
                mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "El dorsal debe ser mayor que 0.");
                return false;
            }

            if (altura <= 0) {
                mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "La altura debe ser mayor que 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "Dorsal debe ser entero y altura decimal.");
            return false;
        }

        return true;
    }

    private boolean validarDorsalDisponible(Integer idEquipo, int dorsal, Integer idJugadorIgnorado) {
        if (idEquipo == null) {
            return true;
        }

        if (jugadorDAO.existeDorsalEnEquipo(idEquipo, dorsal, idJugadorIgnorado)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Dorsal repetido", "Ya existe un jugador con ese dorsal en este equipo.");
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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}