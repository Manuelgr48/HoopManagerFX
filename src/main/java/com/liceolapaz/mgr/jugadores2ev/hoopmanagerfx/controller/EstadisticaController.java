package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAOImpl;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EstadisticaController implements Initializable {

    @FXML private TextField tfBuscador;

    @FXML private TableView<Estadistica> tablaEstadisticas;
    @FXML private TableColumn<Estadistica, Integer> colId;
    @FXML private TableColumn<Estadistica, String> colJugador;
    @FXML private TableColumn<Estadistica, String> colPartido;
    @FXML private TableColumn<Estadistica, Integer> colPuntos;
    @FXML private TableColumn<Estadistica, Integer> colRebotes;
    @FXML private TableColumn<Estadistica, Integer> colAsistencias;
    @FXML private TableColumn<Estadistica, Integer> colFaltas;

    @FXML private ComboBox<Jugador> cbJugador;
    @FXML private ComboBox<Partido> cbPartido;
    @FXML private TextField txtPuntos;
    @FXML private TextField txtRebotes;
    @FXML private TextField txtAsistencias;
    @FXML private TextField txtFaltas;

    @FXML private HBox formularioEstadistica;
    @FXML private HBox botonesCrud;

    @FXML private Button btnAnadir;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    private final EstadisticaDAO estadisticaDAO = new EstadisticaDAOImpl();
    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final PartidoService partidoService = new PartidoService();

    private final ObservableList<Estadistica> listaEstadisticas = FXCollections.observableArrayList();
    private final ObservableList<Jugador> listaJugadores = FXCollections.observableArrayList();
    private final ObservableList<Partido> listaPartidos = FXCollections.observableArrayList();

    private FilteredList<Estadistica> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEstadistica"));
        colJugador.setCellValueFactory(new PropertyValueFactory<>("nombreJugador"));
        colPartido.setCellValueFactory(new PropertyValueFactory<>("equipoRival"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colRebotes.setCellValueFactory(new PropertyValueFactory<>("rebotes"));
        colAsistencias.setCellValueFactory(new PropertyValueFactory<>("asistencias"));
        colFaltas.setCellValueFactory(new PropertyValueFactory<>("faltasCometidas"));

        configurarCombos();
        configurarBuscador();
        configurarPermisos();

        cargarJugadores();
        cargarPartidos();
        cargarEstadisticas();

        tablaEstadisticas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, estadistica) -> {
            if (estadistica != null) {
                rellenarFormulario(estadistica);
            }
        });
    }

    private void configurarCombos() {
        cbJugador.setItems(listaJugadores);
        cbPartido.setItems(listaPartidos);

        cbJugador.setConverter(new StringConverter<>() {
            @Override
            public String toString(Jugador jugador) {
                if (jugador == null) return "";
                return jugador.getNombre() + " " + jugador.getApellidos();
            }

            @Override
            public Jugador fromString(String string) {
                return null;
            }
        });

        cbPartido.setConverter(new StringConverter<>() {
            @Override
            public String toString(Partido partido) {
                if (partido == null) return "";
                return partido.getNombreEquipo() + " vs " + partido.getEquipoRival() + " - " + partido.getFecha();
            }

            @Override
            public Partido fromString(String string) {
                return null;
            }
        });
    }

    private void configurarBuscador() {
        filteredData = new FilteredList<>(listaEstadisticas, estadistica -> true);

        tfBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(estadistica -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String filtro = newValue.toLowerCase();

                return estadistica.getNombreJugador().toLowerCase().contains(filtro)
                        || estadistica.getEquipoRival().toLowerCase().contains(filtro)
                        || String.valueOf(estadistica.getPuntos()).contains(filtro)
                        || String.valueOf(estadistica.getRebotes()).contains(filtro)
                        || String.valueOf(estadistica.getAsistencias()).contains(filtro);
            });
        });

        SortedList<Estadistica> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaEstadisticas.comparatorProperty());
        tablaEstadisticas.setItems(sortedData);
    }

    private void configurarPermisos() {
        boolean esAdmin = SessionManager.getInstance().esAdmin();

        formularioEstadistica.setVisible(esAdmin);
        formularioEstadistica.setManaged(esAdmin);
        botonesCrud.setVisible(esAdmin);
        botonesCrud.setManaged(esAdmin);
    }

    private void cargarJugadores() {
        listaJugadores.clear();
        listaJugadores.addAll(jugadorDAO.obtenerTodos());
    }

    private void cargarPartidos() {
        try {
            listaPartidos.clear();
            listaPartidos.addAll(partidoService.obtenerTodosLosPartidos());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los partidos.");
        }
    }

    private void cargarEstadisticas() {
        listaEstadisticas.clear();
        listaEstadisticas.addAll(estadisticaDAO.obtenerTodas());
    }

    private void rellenarFormulario(Estadistica estadistica) {
        cbJugador.setValue(buscarJugadorPorId(estadistica.getIdJugador()));
        cbPartido.setValue(buscarPartidoPorId(estadistica.getIdPartido()));
        txtPuntos.setText(String.valueOf(estadistica.getPuntos()));
        txtRebotes.setText(String.valueOf(estadistica.getRebotes()));
        txtAsistencias.setText(String.valueOf(estadistica.getAsistencias()));
        txtFaltas.setText(String.valueOf(estadistica.getFaltasCometidas()));
    }

    @FXML
    private void anadirEstadistica() {
        if (!validarCampos()) return;

        Jugador jugador = cbJugador.getValue();
        Partido partido = cbPartido.getValue();

        Estadistica nuevaEstadistica = new Estadistica(
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

        if (estadisticaDAO.insertar(nuevaEstadistica)) {
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

        Jugador jugador = cbJugador.getValue();
        Partido partido = cbPartido.getValue();

        Estadistica estadisticaModificada = new Estadistica(
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

        if (estadisticaDAO.actualizar(estadisticaModificada)) {
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
        cbJugador.setValue(null);
        cbPartido.setValue(null);
        txtPuntos.clear();
        txtRebotes.clear();
        txtAsistencias.clear();
        txtFaltas.clear();
    }

    private boolean validarCampos() {
        if (cbJugador.getValue() == null
                || cbPartido.getValue() == null
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

    private Jugador buscarJugadorPorId(int idJugador) {
        for (Jugador jugador : listaJugadores) {
            if (jugador.getIdJugador() == idJugador) {
                return jugador;
            }
        }

        return null;
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