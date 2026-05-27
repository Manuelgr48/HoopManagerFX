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

        Jugador jugador = new Jugador(
                0,
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                Integer.parseInt(txtDorsal.getText().trim()),
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

        Jugador jugador = new Jugador(
                seleccionado.getIdJugador(),
                txtNombre.getText().trim(),
                txtApellidos.getText().trim(),
                Integer.parseInt(txtDorsal.getText().trim()),
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

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminacion");
        confirmacion.setHeaderText("Seguro que quieres eliminar este jugador?");
        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (jugadorDAO.eliminar(seleccionado.getIdJugador())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Jugador eliminado correctamente.");
                cargarJugadores();
                limpiarFormularioJugador();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el jugador.");
            }
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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}