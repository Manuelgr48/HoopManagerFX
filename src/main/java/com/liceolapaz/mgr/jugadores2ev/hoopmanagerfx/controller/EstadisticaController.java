package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAOImpl;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EstadisticaController implements Initializable {

    @FXML private TableView<Estadistica> tablaEstadisticas;
    @FXML private TableColumn<Estadistica, Integer> colId;
    @FXML private TableColumn<Estadistica, String> colJugador;
    @FXML private TableColumn<Estadistica, Integer> colPartido;
    @FXML private TableColumn<Estadistica, Integer> colPuntos;
    @FXML private TableColumn<Estadistica, Integer> colRebotes;
    @FXML private TableColumn<Estadistica, Integer> colAsistencias;
    @FXML private TableColumn<Estadistica, Integer> colFaltas;

    @FXML private TextField txtJugador;
    @FXML private TextField txtPartido;
    @FXML private TextField txtPuntos;
    @FXML private TextField txtRebotes;
    @FXML private TextField txtAsistencias;
    @FXML private TextField txtFaltas;

    @FXML private Button btnAnadir;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    private EstadisticaDAO estadisticaDAO;
    private JugadorDAO jugadorDAO; // NUEVO
    private ObservableList<Estadistica> listaEstadisticas;

    public EstadisticaController() {
        this.estadisticaDAO = new EstadisticaDAOImpl();
        this.jugadorDAO = new JugadorDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaEstadisticas = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory<>("idEstadistica"));
        colJugador.setCellValueFactory(new PropertyValueFactory<>("nombreJugador"));
        colPartido.setCellValueFactory(new PropertyValueFactory<>("idPartido"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colRebotes.setCellValueFactory(new PropertyValueFactory<>("rebotes"));
        colAsistencias.setCellValueFactory(new PropertyValueFactory<>("asistencias"));
        colFaltas.setCellValueFactory(new PropertyValueFactory<>("faltasCometidas"));

        cargarEstadisticas();
        configurarPermisos();

        tablaEstadisticas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtJugador.setText(newSel.getNombreJugador());
                txtPartido.setText(String.valueOf(newSel.getIdPartido()));
                txtPuntos.setText(String.valueOf(newSel.getPuntos()));
                txtRebotes.setText(String.valueOf(newSel.getRebotes()));
                txtAsistencias.setText(String.valueOf(newSel.getAsistencias()));
                txtFaltas.setText(String.valueOf(newSel.getFaltasCometidas()));
            }
        });
    }

    private void cargarEstadisticas() {
        listaEstadisticas.clear();
        listaEstadisticas.addAll(estadisticaDAO.obtenerTodas());
        tablaEstadisticas.setItems(listaEstadisticas);
    }

    @FXML
    private void anadirEstadistica() {
        if (!validarCampos()) return;

        Integer idJugador = verificarJugador();
        if (idJugador == null) return;

        Estadistica nuevaEstadistica = new Estadistica(0, idJugador, txtJugador.getText(),
                Integer.parseInt(txtPartido.getText()), Integer.parseInt(txtPuntos.getText()),
                Integer.parseInt(txtRebotes.getText()), Integer.parseInt(txtAsistencias.getText()),
                Integer.parseInt(txtFaltas.getText()));

        if (estadisticaDAO.insertar(nuevaEstadistica)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estadística añadida.");
            cargarEstadisticas();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo añadir la estadística.");
        }
    }

    @FXML
    private void modificarEstadistica() {
        Estadistica seleccionado = tablaEstadisticas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona una estadística para modificar.");
            return;
        }
        if (!validarCampos()) return;

        Integer idJugador = verificarJugador();
        if (idJugador == null) return;

        Estadistica estadisticaModificada = new Estadistica(seleccionado.getIdEstadistica(), idJugador, txtJugador.getText(),
                Integer.parseInt(txtPartido.getText()), Integer.parseInt(txtPuntos.getText()),
                Integer.parseInt(txtRebotes.getText()), Integer.parseInt(txtAsistencias.getText()),
                Integer.parseInt(txtFaltas.getText()));

        if (estadisticaDAO.actualizar(estadisticaModificada)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estadística modificada.");
            cargarEstadisticas();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar la estadística.");
        }
    }

    @FXML
    private void eliminarEstadistica() {
        Estadistica seleccionado = tablaEstadisticas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona una estadística para eliminar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar eliminación");
        alerta.setHeaderText("¿Seguro que deseas eliminar esta estadística?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (estadisticaDAO.eliminar(seleccionado.getIdEstadistica())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estadística eliminada.");
                cargarEstadisticas();
                limpiarCampos();
            }
        }
    }

    private Integer verificarJugador() {
        String nombreIntroducido = txtJugador.getText().trim();
        Integer id = jugadorDAO.obtenerIdPorNombre(nombreIntroducido);
        if (id == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El jugador '" + nombreIntroducido + "' no existe. Escribe su nombre o nombre y apellidos exactos.");
        }
        return id;
    }

    @FXML
    private void limpiarCampos() {
        txtJugador.clear(); txtPartido.clear(); txtPuntos.clear();
        txtRebotes.clear(); txtAsistencias.clear(); txtFaltas.clear();
    }

    private boolean validarCampos() {
        if (txtJugador.getText().isEmpty() || txtPartido.getText().isEmpty() || txtPuntos.getText().isEmpty() ||
                txtRebotes.getText().isEmpty() || txtAsistencias.getText().isEmpty() || txtFaltas.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Rellena todos los campos.");
            return false;
        }
        try {
            Integer.parseInt(txtPartido.getText());
            Integer.parseInt(txtPuntos.getText());
            Integer.parseInt(txtRebotes.getText());
            Integer.parseInt(txtAsistencias.getText());
            Integer.parseInt(txtFaltas.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato", "Asegúrate de introducir números en los campos correspondientes.");
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
        if (btnAnadir == null) return;
        if (!"ADMIN".equals(rolActual) && !"ENTRENADOR".equals(rolActual)) {
            btnAnadir.setVisible(false); btnModificar.setVisible(false); btnEliminar.setVisible(false);
            txtJugador.setDisable(true); txtPartido.setDisable(true); txtPuntos.setDisable(true);
            txtRebotes.setDisable(true); txtAsistencias.setDisable(true); txtFaltas.setDisable(true);
        }
    }
}