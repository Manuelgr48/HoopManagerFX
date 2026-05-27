package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAOImpl;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class EstadisticasJugadorController {

    @FXML private Label lblTitulo;

    @FXML private TableView<Estadistica> tablaEstadisticas;
    @FXML private TableColumn<Estadistica, Integer> colId;
    @FXML private TableColumn<Estadistica, String> colRival;
    @FXML private TableColumn<Estadistica, Integer> colPuntos;
    @FXML private TableColumn<Estadistica, Integer> colRebotes;
    @FXML private TableColumn<Estadistica, Integer> colAsistencias;
    @FXML private TableColumn<Estadistica, Integer> colFaltas;

    @FXML private TextField txtPartido;
    @FXML private TextField txtPuntos;
    @FXML private TextField txtRebotes;
    @FXML private TextField txtAsistencias;
    @FXML private TextField txtFaltas;

    @FXML private HBox formularioEstadistica;
    @FXML private HBox botonesCrud;

    private final EstadisticaDAO estadisticaDAO = new EstadisticaDAOImpl();
    private final ObservableList<Estadistica> listaEstadisticas = FXCollections.observableArrayList();

    private Jugador jugador;
    private boolean puedeModificar;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEstadistica"));
        colRival.setCellValueFactory(new PropertyValueFactory<>("equipoRival"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colRebotes.setCellValueFactory(new PropertyValueFactory<>("rebotes"));
        colAsistencias.setCellValueFactory(new PropertyValueFactory<>("asistencias"));
        colFaltas.setCellValueFactory(new PropertyValueFactory<>("faltasCometidas"));

        tablaEstadisticas.setItems(listaEstadisticas);

        tablaEstadisticas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, estadistica) -> {
            if (estadistica != null) {
                txtPartido.setText(String.valueOf(estadistica.getIdPartido()));
                txtPuntos.setText(String.valueOf(estadistica.getPuntos()));
                txtRebotes.setText(String.valueOf(estadistica.getRebotes()));
                txtAsistencias.setText(String.valueOf(estadistica.getAsistencias()));
                txtFaltas.setText(String.valueOf(estadistica.getFaltasCometidas()));
            }
        });
    }

    public void configurar(Jugador jugador, boolean puedeModificar) {
        this.jugador = jugador;
        this.puedeModificar = puedeModificar;

        lblTitulo.setText("Estadisticas de " + jugador.getNombre() + " " + jugador.getApellidos());

        formularioEstadistica.setVisible(puedeModificar);
        formularioEstadistica.setManaged(puedeModificar);
        botonesCrud.setVisible(puedeModificar);
        botonesCrud.setManaged(puedeModificar);

        cargarEstadisticas();
    }

    private void cargarEstadisticas() {
        listaEstadisticas.clear();
        listaEstadisticas.addAll(estadisticaDAO.obtenerPorJugador(jugador.getIdJugador()));
    }

    @FXML
    private void anadirEstadistica() {
        if (!validarCampos()) return;

        Estadistica estadistica = new Estadistica(
                0,
                jugador.getIdJugador(),
                jugador.getNombre() + " " + jugador.getApellidos(),
                Integer.parseInt(txtPartido.getText().trim()),
                Integer.parseInt(txtPuntos.getText().trim()),
                Integer.parseInt(txtRebotes.getText().trim()),
                Integer.parseInt(txtAsistencias.getText().trim()),
                Integer.parseInt(txtFaltas.getText().trim())
        );

        if (estadisticaDAO.insertar(estadistica)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Estadistica anadida correctamente.");
            cargarEstadisticas();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo anadir la estadistica.");
        }
    }

    @FXML
    private void modificarEstadistica() {
        Estadistica seleccionada = tablaEstadisticas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona una estadistica.");
            return;
        }

        if (!validarCampos()) return;

        Estadistica estadistica = new Estadistica(
                seleccionada.getIdEstadistica(),
                jugador.getIdJugador(),
                jugador.getNombre() + " " + jugador.getApellidos(),
                Integer.parseInt(txtPartido.getText().trim()),
                Integer.parseInt(txtPuntos.getText().trim()),
                Integer.parseInt(txtRebotes.getText().trim()),
                Integer.parseInt(txtAsistencias.getText().trim()),
                Integer.parseInt(txtFaltas.getText().trim())
        );

        if (estadisticaDAO.actualizar(estadistica)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Estadistica modificada correctamente.");
            cargarEstadisticas();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar la estadistica.");
        }
    }

    @FXML
    private void eliminarEstadistica() {
        Estadistica seleccionada = tablaEstadisticas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona una estadistica.");
            return;
        }

        if (estadisticaDAO.eliminar(seleccionada.getIdEstadistica())) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Estadistica eliminada correctamente.");
            cargarEstadisticas();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar la estadistica.");
        }
    }

    @FXML
    private void limpiarCampos() {
        tablaEstadisticas.getSelectionModel().clearSelection();
        txtPartido.clear();
        txtPuntos.clear();
        txtRebotes.clear();
        txtAsistencias.clear();
        txtFaltas.clear();
    }

    private boolean validarCampos() {
        if (txtPartido.getText().trim().isEmpty()
                || txtPuntos.getText().trim().isEmpty()
                || txtRebotes.getText().trim().isEmpty()
                || txtAsistencias.getText().trim().isEmpty()
                || txtFaltas.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Rellena todos los campos.");
            return false;
        }

        try {
            Integer.parseInt(txtPartido.getText().trim());
            Integer.parseInt(txtPuntos.getText().trim());
            Integer.parseInt(txtRebotes.getText().trim());
            Integer.parseInt(txtAsistencias.getText().trim());
            Integer.parseInt(txtFaltas.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "Todos los campos deben ser numeros enteros.");
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
}