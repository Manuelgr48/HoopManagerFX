package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PartidosController implements Initializable {

    @FXML private TableView<Partido> tablaPartidos;
    @FXML private TableColumn<Partido, String> colEquipo;
    @FXML private TableColumn<Partido, LocalDate> colFecha;
    @FXML private TableColumn<Partido, String> colRival;
    @FXML private TableColumn<Partido, String> colUbicacion;
    @FXML private TableColumn<Partido, Integer> colResPropio;
    @FXML private TableColumn<Partido, Integer> colResRival;

    @FXML private TextField tfEquipo;
    @FXML private DatePicker dpFecha;
    @FXML private TextField tfRival;
    @FXML private TextField tfUbicacion;
    @FXML private TextField tfResPropio;
    @FXML private TextField tfResRival;

    @FXML private HBox formularioPartido;
    @FXML private HBox botonesCrud;
    @FXML private Button btnAnadir;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final PartidoService partidoService = new PartidoService();
    private final ObservableList<Partido> listaPartidos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEquipo.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colRival.setCellValueFactory(new PropertyValueFactory<>("equipoRival"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colResPropio.setCellValueFactory(new PropertyValueFactory<>("resultadoPropio"));
        colResRival.setCellValueFactory(new PropertyValueFactory<>("resultadoRival"));

        tablaPartidos.setItems(listaPartidos);

        tablaPartidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, partido) -> {
            if (partido != null) {
                rellenarFormulario(partido);
            }
        });

        configurarPermisos();
        cargarPartidos();
    }

    private void configurarPermisos() {
        boolean puedeModificar = SessionManager.getInstance().esAdmin();

        formularioPartido.setVisible(puedeModificar);
        formularioPartido.setManaged(puedeModificar);
        botonesCrud.setVisible(puedeModificar);
        botonesCrud.setManaged(puedeModificar);
    }

    private void cargarPartidos() {
        Task<List<Partido>> task = new Task<>() {
            @Override
            protected List<Partido> call() throws Exception {
                return partidoService.obtenerTodosLosPartidos();
            }
        };

        task.setOnSucceeded(event -> listaPartidos.setAll(task.getValue()));

        task.setOnFailed(event -> {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los partidos.");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    private void handleAnadir() {
        if (!validarFormulario()) {
            return;
        }

        Partido partido = crearPartidoDesdeFormulario();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                partidoService.registrarPartido(partido);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Partido anadido correctamente.");
            cargarPartidos();
            limpiarFormulario();
        });

        task.setOnFailed(event -> mostrarAlerta(Alert.AlertType.ERROR, "Error", task.getException().getMessage()));

        new Thread(task).start();
    }

    @FXML
    private void handleModificar() {
        Partido seleccionado = tablaPartidos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un partido.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Partido partido = crearPartidoDesdeFormulario();
        partido.setIdPartido(seleccionado.getIdPartido());

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                partidoService.actualizarPartido(partido);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Partido modificado correctamente.");
            cargarPartidos();
            limpiarFormulario();
        });

        task.setOnFailed(event -> mostrarAlerta(Alert.AlertType.ERROR, "Error", task.getException().getMessage()));

        new Thread(task).start();
    }

    @FXML
    private void handleEliminar() {
        Partido seleccionado = tablaPartidos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un partido.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminacion");
        confirmacion.setHeaderText("Seguro que quieres eliminar este partido?");
        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    partidoService.eliminarPartido(seleccionado.getIdPartido());
                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Partido eliminado correctamente.");
                cargarPartidos();
                limpiarFormulario();
            });

            task.setOnFailed(event -> mostrarAlerta(Alert.AlertType.ERROR, "Error", task.getException().getMessage()));

            new Thread(task).start();
        }
    }

    private Partido crearPartidoDesdeFormulario() {
        Integer idEquipo = obtenerIdEquipoDesdeTexto();

        return new Partido(
                idEquipo,
                dpFecha.getValue(),
                tfRival.getText().trim(),
                tfUbicacion.getText().trim(),
                Integer.parseInt(tfResPropio.getText().trim()),
                Integer.parseInt(tfResRival.getText().trim())
        );
    }

    private void rellenarFormulario(Partido partido) {
        tfEquipo.setText(partido.getNombreEquipo().equals("Sin equipo") ? "" : partido.getNombreEquipo());
        dpFecha.setValue(partido.getFecha());
        tfRival.setText(partido.getEquipoRival());
        tfUbicacion.setText(partido.getUbicacion());
        tfResPropio.setText(String.valueOf(partido.getResultadoPropio()));
        tfResRival.setText(String.valueOf(partido.getResultadoRival()));
    }

    @FXML
    private void limpiarFormulario() {
        tablaPartidos.getSelectionModel().clearSelection();
        tfEquipo.clear();
        dpFecha.setValue(null);
        tfRival.clear();
        tfUbicacion.clear();
        tfResPropio.clear();
        tfResRival.clear();
    }

    private boolean validarFormulario() {
        if (tfEquipo.getText().trim().isEmpty()
                || dpFecha.getValue() == null
                || tfRival.getText().trim().isEmpty()
                || tfUbicacion.getText().trim().isEmpty()
                || tfResPropio.getText().trim().isEmpty()
                || tfResRival.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Rellena todos los campos.");
            return false;
        }

        try {
            Integer.parseInt(tfResPropio.getText().trim());
            Integer.parseInt(tfResRival.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "Los resultados deben ser numeros enteros.");
            return false;
        }

        return true;
    }

    private Integer obtenerIdEquipoDesdeTexto() {
        String nombreEquipo = tfEquipo.getText().trim();

        Integer idEquipo = equipoDAO.obtenerIdPorNombre(nombreEquipo);

        if (idEquipo == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Equipo no encontrado", "El equipo '" + nombreEquipo + "' no existe.");
        }

        return idEquipo;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}