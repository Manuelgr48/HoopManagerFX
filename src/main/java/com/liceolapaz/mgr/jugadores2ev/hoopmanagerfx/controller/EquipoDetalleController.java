package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.EquipoSeleccionadoContext;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.ReporteService;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;

public class EquipoDetalleController implements Initializable {

    @FXML private Label lblTituloEquipo;
    @FXML private Label lblCategoria;
    @FXML private Label lblPresupuesto;

    @FXML private TableView<Jugador> tablaJugadores;
    @FXML private TableColumn<Jugador, Integer> colId;
    @FXML private TableColumn<Jugador, String> colNombre;
    @FXML private TableColumn<Jugador, String> colApellidos;
    @FXML private TableColumn<Jugador, Integer> colDorsal;
    @FXML private TableColumn<Jugador, String> colPosicion;
    @FXML private TableColumn<Jugador, Double> colAltura;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDorsal;
    @FXML private TextField txtPosicion;
    @FXML private TextField txtAltura;

    @FXML private GridPane formularioJugador;
    @FXML private HBox botonesCrud;
    @FXML private Button btnVerEstadisticas;

    @FXML private BarChart<String, Number> graficoRendimiento;

    private final JugadorDAO jugadorDAO = new JugadorDAO();
    private final PartidoService partidoService = new PartidoService();
    private final ObservableList<Jugador> listaJugadores = FXCollections.observableArrayList();

    private Equipo equipo;
    private boolean puedeModificar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        equipo = EquipoSeleccionadoContext.getEquipoSeleccionado();

        if (equipo == null) {
            lblTituloEquipo.setText("No hay equipo seleccionado");
            return;
        }

        SessionManager sesion = SessionManager.getInstance();
        puedeModificar = sesion.esAdmin()
                || (sesion.esEntrenador()
                && sesion.getIdEquipo() != null
                && sesion.getIdEquipo().equals(equipo.getIdEquipo()));

        configurarCabecera();
        configurarTabla();
        configurarPermisos();
        cargarJugadores();
        cargarGraficoRendimiento();
    }
    @FXML
    private void generarInformeEquipo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar informe del equipo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        String nombreArchivo = "Informe_" + equipo.getNombre().replace(" ", "_") + ".pdf";
        fileChooser.setInitialFileName(nombreArchivo);

        File file = fileChooser.showSaveDialog(lblTituloEquipo.getScene().getWindow());

        if (file == null) {
            return;
        }

        try {
            ReporteService reporteService = new ReporteService();
            reporteService.generarInformeEquipo(file.getAbsolutePath(), equipo.getIdEquipo(), equipo.getNombre());

            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Informe generado correctamente.");

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo generar el informe: " + e.getMessage());
        }
    }

    private void configurarCabecera() {
        lblTituloEquipo.setText(equipo.getNombre());
        lblCategoria.setText("Categoria: " + equipo.getCategoria());
        lblPresupuesto.setText("Presupuesto: " + equipo.getPresupuesto());
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDorsal.setCellValueFactory(new PropertyValueFactory<>("dorsal"));
        colPosicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));

        tablaJugadores.setItems(listaJugadores);

        tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, jugador) -> {
            btnVerEstadisticas.setDisable(jugador == null);

            if (jugador != null) {
                txtNombre.setText(jugador.getNombre());
                txtApellidos.setText(jugador.getApellidos());
                txtDorsal.setText(String.valueOf(jugador.getDorsal()));
                txtPosicion.setText(jugador.getPosicion());
                txtAltura.setText(String.valueOf(jugador.getAltura()));
            }
        });
    }

    private void configurarPermisos() {
        formularioJugador.setVisible(puedeModificar);
        formularioJugador.setManaged(puedeModificar);
        botonesCrud.setVisible(puedeModificar);
        botonesCrud.setManaged(puedeModificar);
        btnVerEstadisticas.setDisable(true);
    }

    private void cargarJugadores() {
        listaJugadores.clear();
        listaJugadores.addAll(jugadorDAO.obtenerPorEquipo(equipo.getIdEquipo()));
    }

    private void cargarGraficoRendimiento() {
        graficoRendimiento.getData().clear();

        Task<List<Partido>> task = new Task<>() {
            @Override
            protected List<Partido> call() throws Exception {
                return partidoService.obtenerPartidosPorEquipo(equipo.getIdEquipo());
            }
        };

        task.setOnSucceeded(event -> {
            XYChart.Series<String, Number> anotados = new XYChart.Series<>();
            anotados.setName("Anotados");

            XYChart.Series<String, Number> recibidos = new XYChart.Series<>();
            recibidos.setName("Recibidos");

            for (Partido partido : task.getValue()) {
                anotados.getData().add(new XYChart.Data<>(partido.getEquipoRival(), partido.getResultadoPropio()));
                recibidos.getData().add(new XYChart.Data<>(partido.getEquipoRival(), partido.getResultadoRival()));
            }

            graficoRendimiento.getData().addAll(anotados, recibidos);
        });

        task.setOnFailed(event -> {
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    private void anadirJugador() {
        if (!validarCamposJugador()) return;

        int dorsal = Integer.parseInt(txtDorsal.getText().trim());

        if (!validarDorsalDisponible(dorsal, null)) {
            return;
        }

        Jugador jugador = new Jugador(
                0,
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                dorsal,
                txtPosicion.getText().trim(),
                Double.parseDouble(txtAltura.getText().trim()),
                equipo.getIdEquipo(),
                equipo.getNombre()
        );

        if (jugadorDAO.insertar(jugador)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Jugador anadido correctamente.");
            cargarJugadores();
            limpiarFormularioJugador();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo anadir el jugador.");
        }
    }

    @FXML
    private void modificarJugador() {
        Jugador seleccionado = tablaJugadores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un jugador.");
            return;
        }

        if (!validarCamposJugador()) return;

        if (!confirmar("Confirmar modificacion", "Seguro que quieres modificar este jugador?")) {
            return;
        }

        int dorsal = Integer.parseInt(txtDorsal.getText().trim());

        if (!validarDorsalDisponible(dorsal, seleccionado.getIdJugador())) {
            return;
        }

        Jugador jugador = new Jugador(
                seleccionado.getIdJugador(),
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                dorsal,
                txtPosicion.getText().trim(),
                Double.parseDouble(txtAltura.getText().trim()),
                equipo.getIdEquipo(),
                equipo.getNombre()
        );

        if (jugadorDAO.actualizar(jugador)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Jugador modificado correctamente.");
            cargarJugadores();
            limpiarFormularioJugador();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar el jugador.");
        }
    }

    @FXML
    private void eliminarJugador() {
        Jugador seleccionado = tablaJugadores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un jugador.");
            return;
        }

        if (!confirmar("Confirmar eliminacion", "Seguro que quieres eliminar este jugador?")) {
            return;
        }

        if (jugadorDAO.eliminar(seleccionado.getIdJugador())) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Jugador eliminado correctamente.");
            cargarJugadores();
            limpiarFormularioJugador();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el jugador.");
        }
    }

    @FXML
    private void verEstadisticasJugador() {
        Jugador jugador = tablaJugadores.getSelectionModel().getSelectedItem();

        if (jugador == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un jugador.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/estadisticas-jugador-view.fxml"));
            Scene scene = new Scene(loader.load());

            EstadisticasJugadorController controller = loader.getController();
            controller.configurar(jugador, puedeModificar);

            Stage stage = new Stage();
            stage.setTitle("Estadisticas de " + jugador.getNombre() + " " + jugador.getApellidos());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de estadisticas.");
        }
    }

    @FXML
    private void limpiarFormularioJugador() {
        tablaJugadores.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtApellidos.clear();
        txtDorsal.clear();
        txtPosicion.clear();
        txtAltura.clear();
    }

    private boolean validarCamposJugador() {
        if (txtNombre.getText().trim().isEmpty()
                || txtApellidos.getText().trim().isEmpty()
                || txtDorsal.getText().trim().isEmpty()
                || txtPosicion.getText().trim().isEmpty()
                || txtAltura.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Rellena todos los campos del jugador.");
            return false;
        }

        try {
            Integer.parseInt(txtDorsal.getText().trim());
            Double.parseDouble(txtAltura.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "Dorsal debe ser entero y altura decimal.");
            return false;
        }

        return true;
    }
    private boolean validarDorsalDisponible(int dorsal, Integer idJugadorIgnorado) {
        if (jugadorDAO.existeDorsalEnEquipo(equipo.getIdEquipo(), dorsal, idJugadorIgnorado)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Dorsal repetido", "Ya existe un jugador con ese dorsal en este equipo.");
            return false;
        }

        return true;
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