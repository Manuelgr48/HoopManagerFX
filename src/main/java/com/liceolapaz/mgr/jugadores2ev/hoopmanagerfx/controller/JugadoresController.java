package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class JugadoresController implements Initializable {

    @FXML private TableView<Jugador> tablaJugadores;
    @FXML private TableColumn<Jugador, Integer> colId;
    @FXML private TableColumn<Jugador, String> colNombre;
    @FXML private TableColumn<Jugador, String> colApellidos;
    @FXML private TableColumn<Jugador, Integer> colDorsal;
    @FXML private TableColumn<Jugador, String> colPosicion;
    @FXML private TableColumn<Jugador, Double> colAltura;
    @FXML private TableColumn<Jugador, String> colEquipo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDorsal;
    @FXML private TextField txtPosicion;
    @FXML private TextField txtAltura;
    @FXML private TextField txtEquipo;
    @FXML private TextField tfBuscador;

    @FXML private Button btnAnadir;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    private JugadorDAO jugadorDAO;
    private EquipoDAO equipoDAO;
    private ObservableList<Jugador> listaJugadores;

    public JugadoresController() {
        this.jugadorDAO = new JugadorDAO();
        this.equipoDAO = new EquipoDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaJugadores = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDorsal.setCellValueFactory(new PropertyValueFactory<>("dorsal"));
        colPosicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        colEquipo.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));

        cargarJugadores();
        configurarPermisos();

        tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombre.setText(newSelection.getNombre());
                txtApellidos.setText(newSelection.getApellidos());
                txtDorsal.setText(String.valueOf(newSelection.getDorsal()));
                txtPosicion.setText(newSelection.getPosicion());
                txtAltura.setText(String.valueOf(newSelection.getAltura()));
                txtEquipo.setText(newSelection.getNombreEquipo().equals("Agente Libre") ? "" : newSelection.getNombreEquipo());
            }
        });

        FilteredList<Jugador> filteredData = new FilteredList<>(listaJugadores, p -> true);
        tfBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(jugador -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return jugador.getNombre().toLowerCase().contains(lowerCaseFilter) ||
                        jugador.getApellidos().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Jugador> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaJugadores.comparatorProperty());
        tablaJugadores.setItems(sortedData);
    }

    private void cargarJugadores() {
        listaJugadores.clear();
        listaJugadores.addAll(jugadorDAO.obtenerTodos());
    }

    @FXML
    private void anadirJugador() {
        if (!validarCampos()) return;
        Integer idEquipo = obtenerIdEquipoDesdeTextField();
        if (idEquipo == null && !txtEquipo.getText().trim().isEmpty()) return;

        Jugador nuevoJugador = new Jugador(0, txtNombre.getText(), txtApellidos.getText(),
                Integer.parseInt(txtDorsal.getText()), txtPosicion.getText(), Double.parseDouble(txtAltura.getText()),
                idEquipo, null);

        if (jugadorDAO.insertar(nuevoJugador)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Jugador añadido correctamente.");
            cargarJugadores();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo añadir el jugador.");
        }
    }

    @FXML
    private void modificarJugador() {
        Jugador seleccionado = tablaJugadores.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona un jugador para modificar.");
            return;
        }
        if (!validarCampos()) return;
        Integer idEquipo = obtenerIdEquipoDesdeTextField();
        if (idEquipo == null && !txtEquipo.getText().trim().isEmpty()) return;

        Jugador jugadorModificado = new Jugador(seleccionado.getIdJugador(), txtNombre.getText(), txtApellidos.getText(),
                Integer.parseInt(txtDorsal.getText()), txtPosicion.getText(), Double.parseDouble(txtAltura.getText()),
                idEquipo, null);

        if (jugadorDAO.actualizar(jugadorModificado)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Jugador modificado correctamente.");
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

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar eliminación");
        alerta.setHeaderText("¿Estás seguro de que quieres eliminar a este jugador?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (jugadorDAO.eliminar(seleccionado.getIdJugador())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Jugador eliminado correctamente.");
                cargarJugadores();
                limpiarCampos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el jugador.");
            }
        }
    }

    private Integer obtenerIdEquipoDesdeTextField() {
        String nombreIntroducido = txtEquipo.getText().trim();
        if (nombreIntroducido.isEmpty()) return null;

        Integer id = equipoDAO.obtenerIdPorNombre(nombreIntroducido);
        if (id == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Equipo", "El equipo '" + nombreIntroducido + "' no existe en la base de datos. Por favor, revisa el nombre o déjalo vacío.");
        }
        return id;
    }

    private void limpiarCampos() {
        txtNombre.clear(); txtApellidos.clear(); txtDorsal.clear(); txtPosicion.clear(); txtAltura.clear(); txtEquipo.clear();
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtDorsal.getText().isEmpty() ||
                txtPosicion.getText().isEmpty() || txtAltura.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Por favor, rellena todos los campos obligatorios.");
            return false;
        }
        try {
            Integer.parseInt(txtDorsal.getText());
            Double.parseDouble(txtAltura.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "Dorsal debe ser un número entero y Altura un número decimal.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void configurarPermisos() {
        String rolActual = SessionManager.getInstance().getRol();
        Integer equipoEntrenador = SessionManager.getInstance().getIdEquipo();

        if (btnAnadir == null) return;

        if ("JUGADOR".equals(rolActual)) {
            btnAnadir.setVisible(false); btnModificar.setVisible(false); btnEliminar.setVisible(false);
            txtNombre.setDisable(true); txtApellidos.setDisable(true); txtDorsal.setDisable(true);
            txtPosicion.setDisable(true); txtAltura.setDisable(true); txtEquipo.setDisable(true);
        } else if ("ENTRENADOR".equals(rolActual)) {
            btnAnadir.setVisible(true);
            tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    boolean esDeSuEquipo = equipoEntrenador != null && newSel.getIdEquipo() != null && (newSel.getIdEquipo().equals(equipoEntrenador));
                    btnModificar.setDisable(!esDeSuEquipo);
                    btnEliminar.setDisable(!esDeSuEquipo);
                }
            });
        } else if ("ADMIN".equals(rolActual)) {
            btnAnadir.setVisible(true); btnModificar.setVisible(true); btnEliminar.setVisible(true);
        }
    }
}