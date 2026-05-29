package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAOImpl;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.Optional;

public class EstadisticasJugadorController {

    @FXML private Label lblTitulo;

    @FXML private TableView<Estadistica> tablaEstadisticas;
    @FXML private TableColumn<Estadistica, String> colRival;
    @FXML private TableColumn<Estadistica, LocalDate> colFecha;
    @FXML private TableColumn<Estadistica, Integer> colPuntos;
    @FXML private TableColumn<Estadistica, Integer> colRebotes;
    @FXML private TableColumn<Estadistica, Integer> colAsistencias;
    @FXML private TableColumn<Estadistica, Integer> colFaltas;

    @FXML private ComboBox<Partido> cbPartido;
    @FXML private TextField txtPuntos;
    @FXML private TextField txtRebotes;
    @FXML private TextField txtAsistencias;
    @FXML private TextField txtFaltas;

    @FXML private HBox formularioEstadistica;
    @FXML private HBox botonesCrud;

    private final EstadisticaDAO estadisticaDAO = new EstadisticaDAOImpl();
    private final PartidoService partidoService = new PartidoService();

    private final ObservableList<Estadistica> listaEstadisticas = FXCollections.observableArrayList();
    private final ObservableList<Partido> listaPartidos = FXCollections.observableArrayList();

    private Jugador jugador;
    private boolean puedeModificar;

    @FXML
    public void initialize() {
        colRival.setCellValueFactory(new PropertyValueFactory<>("equipoRival"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPartido"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colRebotes.setCellValueFactory(new PropertyValueFactory<>("rebotes"));
        colAsistencias.setCellValueFactory(new PropertyValueFactory<>("asistencias"));
        colFaltas.setCellValueFactory(new PropertyValueFactory<>("faltasCometidas"));

        tablaEstadisticas.setItems(listaEstadisticas);
        cbPartido.setItems(listaPartidos);

        cbPartido.setConverter(new StringConverter<>() {
            @Override
            public String toString(Partido partido) {
                if (partido == null) return "";
                return partido.getEquipoRival() + " - " + partido.getFecha();
            }

            @Override
            public Partido fromString(String string) {
                return null;
            }
        });

        tablaEstadisticas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, estadistica) -> {
            if (estadistica != null) {
                cbPartido.setValue(buscarPartidoPorId(estadistica.getIdPartido()));
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

        cargarPartidosJugador();
        cargarEstadisticas();
    }

    private void cargarPartidosJugador() {
        try {
            listaPartidos.clear();

            if (jugador.getIdEquipo() != null) {
                listaPartidos.addAll(partidoService.obtenerPartidosPorEquipo(jugador.getIdEquipo()));
            } else {
                listaPartidos.addAll(partidoService.obtenerTodosLosPartidos());
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los partidos.");
        }
    }

    private void cargarEstadisticas() {
        listaEstadisticas.clear();
        listaEstadisticas.addAll(estadisticaDAO.obtenerPorJugador(jugador.getIdJugador()));
    }

    @FXML
    private void anadirEstadistica() {
        if (!validarCampos()) return;

        Partido partido = cbPartido.getValue();

        Estadistica estadistica = new Estadistica(
                0,
                jugador.getIdJugador(),
                jugador.getNombre() + " " + jugador.getApellidos(),
                partido.getIdPartido(),
                partido.getEquipoRival(),
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

        if (!confirmar("Confirmar modificacion", "Seguro que quieres modificar esta estadistica?")) {
            return;
        }

        Partido partido = cbPartido.getValue();

        Estadistica estadistica = new Estadistica(
                seleccionada.getIdEstadistica(),
                jugador.getIdJugador(),
                jugador.getNombre() + " " + jugador.getApellidos(),
                partido.getIdPartido(),
                partido.getEquipoRival(),
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

        if (!confirmar("Confirmar eliminacion", "Seguro que quieres eliminar esta estadistica?")) {
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
        cbPartido.setValue(null);
        txtPuntos.clear();
        txtRebotes.clear();
        txtAsistencias.clear();
        txtFaltas.clear();
    }

    private boolean validarCampos() {
        if (cbPartido.getValue() == null
                || txtPuntos.getText().trim().isEmpty()
                || txtRebotes.getText().trim().isEmpty()
                || txtAsistencias.getText().trim().isEmpty()
                || txtFaltas.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Rellena todos los campos.");
            return false;
        }

        try {
            int puntos = Integer.parseInt(txtPuntos.getText().trim());
            int rebotes = Integer.parseInt(txtRebotes.getText().trim());
            int asistencias = Integer.parseInt(txtAsistencias.getText().trim());
            int faltas = Integer.parseInt(txtFaltas.getText().trim());

            if (puntos < 0 || rebotes < 0 || asistencias < 0 || faltas < 0) {
                mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "Los valores no pueden ser negativos.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "Puntos, rebotes, asistencias y faltas deben ser numeros enteros.");
            return false;
        }

        return true;
    }

    private Partido buscarPartidoPorId(int idPartido) {
        for (Partido partido : listaPartidos) {
            if (partido.getIdPartido() == idPartido) {
                return partido;
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