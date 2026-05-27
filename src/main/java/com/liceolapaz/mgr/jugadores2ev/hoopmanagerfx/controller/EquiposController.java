package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.EquipoSeleccionadoContext;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EquiposController implements Initializable {

    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TableColumn<Equipo, Integer> colId;
    @FXML private TableColumn<Equipo, String> colNombre;
    @FXML private TableColumn<Equipo, String> colCategoria;
    @FXML private TableColumn<Equipo, Double> colPresupuesto;
    @FXML private TableColumn<Equipo, String> colFechaCreacion;

    @FXML private TextField tfNombre;
    @FXML private TextField tfCategoria;
    @FXML private TextField tfPresupuesto;
    @FXML private DatePicker dpFechaCreacion;

    @FXML private GridPane formularioEquipo;
    @FXML private Button btnAnadir;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnAccederInformacion;

    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final ObservableList<Equipo> listaEquipos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPresupuesto.setCellValueFactory(new PropertyValueFactory<>("presupuesto"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));

        tablaEquipos.setItems(listaEquipos);
        btnAccederInformacion.setDisable(true);

        tablaEquipos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, equipo) -> {
            btnAccederInformacion.setDisable(equipo == null);

            if (equipo != null) {
                tfNombre.setText(equipo.getNombre());
                tfCategoria.setText(equipo.getCategoria());
                tfPresupuesto.setText(String.valueOf(equipo.getPresupuesto()));
                dpFechaCreacion.setValue(equipo.getFechaCreacion());
            }
        });

        configurarPermisos();
        cargarEquipos();
    }

    private void configurarPermisos() {
        boolean esAdmin = SessionManager.getInstance().esAdmin();

        formularioEquipo.setVisible(esAdmin);
        formularioEquipo.setManaged(esAdmin);
        btnAnadir.setVisible(esAdmin);
        btnAnadir.setManaged(esAdmin);
        btnModificar.setVisible(esAdmin);
        btnModificar.setManaged(esAdmin);
        btnEliminar.setVisible(esAdmin);
        btnEliminar.setManaged(esAdmin);
    }

    private void cargarEquipos() {
        listaEquipos.clear();
        listaEquipos.addAll(equipoDAO.obtenerTodos());
    }

    @FXML
    private void accederInformacionEquipo() {
        Equipo equipo = tablaEquipos.getSelectionModel().getSelectedItem();

        if (equipo == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un equipo.");
            return;
        }

        EquipoSeleccionadoContext.setEquipoSeleccionado(equipo);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liceolapaz/mgr/jugadores2ev/hoopmanagerfx/equipo-detalle-view.fxml"));
            Node vista = loader.load();

            StackPane contentArea = (StackPane) tablaEquipos.getScene().lookup("#contentArea");
            contentArea.getChildren().setAll(vista);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo abrir la informacion del equipo.");
        }
    }

    @FXML
    private void handleAnadir() {
        if (!validarFormulario()) return;

        Equipo equipo = new Equipo(
                0,
                tfNombre.getText().trim(),
                tfCategoria.getText().trim(),
                Double.parseDouble(tfPresupuesto.getText().trim()),
                dpFechaCreacion.getValue()
        );

        if (equipoDAO.insertar(equipo)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Equipo anadido correctamente.");
            limpiarFormulario();
            cargarEquipos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo anadir el equipo.");
        }
    }

    @FXML
    private void handleModificar() {
        Equipo equipo = tablaEquipos.getSelectionModel().getSelectedItem();

        if (equipo == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un equipo.");
            return;
        }

        if (!validarFormulario()) return;

        equipo.setNombre(tfNombre.getText().trim());
        equipo.setCategoria(tfCategoria.getText().trim());
        equipo.setPresupuesto(Double.parseDouble(tfPresupuesto.getText().trim()));
        equipo.setFechaCreacion(dpFechaCreacion.getValue());

        if (equipoDAO.modificar(equipo)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Equipo modificado correctamente.");
            limpiarFormulario();
            cargarEquipos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo modificar el equipo.");
        }
    }

    @FXML
    private void handleEliminar() {
        Equipo equipo = tablaEquipos.getSelectionModel().getSelectedItem();

        if (equipo == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un equipo.");
            return;
        }

        if (equipoDAO.eliminar(equipo.getIdEquipo())) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Equipo eliminado correctamente.");
            limpiarFormulario();
            cargarEquipos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el equipo.");
        }
    }

    @FXML
    private void limpiarFormulario() {
        tablaEquipos.getSelectionModel().clearSelection();
        tfNombre.clear();
        tfCategoria.clear();
        tfPresupuesto.clear();
        dpFechaCreacion.setValue(null);
    }

    private boolean validarFormulario() {
        if (tfNombre.getText().trim().isEmpty()
                || tfCategoria.getText().trim().isEmpty()
                || tfPresupuesto.getText().trim().isEmpty()
                || dpFechaCreacion.getValue() == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Campos incompletos", "Rellena todos los campos.");
            return false;
        }

        try {
            Double.parseDouble(tfPresupuesto.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato incorrecto", "El presupuesto debe ser numerico.");
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