package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class EquiposController implements Initializable {

    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TableColumn<Equipo, Integer> colId;
    @FXML private TableColumn<Equipo, String> colNombre;
    @FXML private TableColumn<Equipo, String> colCategoria;
    @FXML private TableColumn<Equipo, Double> colPresupuesto;
    @FXML private TableColumn<Equipo, String> colFechaCreacion;

    @FXML private javafx.scene.control.TextField tfNombre;
    @FXML private javafx.scene.control.TextField tfCategoria;
    @FXML private javafx.scene.control.TextField tfPresupuesto;
    @FXML private javafx.scene.control.DatePicker dpFechaCreacion;
    @FXML private javafx.scene.control.Button btnAnadir;


    private EquipoDAO equipoDAO;
    private ObservableList<Equipo> listaEquipos;

    public EquiposController() {

        this.equipoDAO = new EquipoDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaEquipos = FXCollections.observableArrayList();

        this.colId.setCellValueFactory(new PropertyValueFactory<>("id_equipo"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        this.colPresupuesto.setCellValueFactory(new PropertyValueFactory<>("presupuesto"));
        this.colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fecha_creacion"));

        cargarEquipos();
    }

    private void cargarEquipos() {
        javafx.concurrent.Task<java.util.List<Equipo>> tareaCarga = new javafx.concurrent.Task<>() {
            @Override
            protected java.util.List<Equipo> call() throws Exception {
                return equipoDAO.obtenerTodos();
            }
        };
        tareaCarga.setOnSucceeded(event -> {
            listaEquipos.clear();
            listaEquipos.addAll(tareaCarga.getValue());
            tablaEquipos.setItems(listaEquipos);
        });
        tareaCarga.setOnFailed(event -> {
            System.err.println("Fallo al cargar los equipos desde la BD.");
            tareaCarga.getException().printStackTrace();
        });
        new Thread(tareaCarga).start();
    }
    @FXML
    private void handleAnadir() {
        if (tfNombre.getText().isEmpty() || tfCategoria.getText().isEmpty() ||
                tfPresupuesto.getText().isEmpty() || dpFechaCreacion.getValue() == null) {
            System.err.println("Faltan campos por rellenar");
            return;
        }
        Equipo nuevoEquipo = new Equipo(
                0,
                tfNombre.getText(),
                tfCategoria.getText(),
                Double.parseDouble(tfPresupuesto.getText()),
                dpFechaCreacion.getValue()
        );

        javafx.concurrent.Task<Boolean> tareaInsertar = new javafx.concurrent.Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return equipoDAO.insertar(nuevoEquipo);
            }
        };


        tareaInsertar.setOnSucceeded(event -> {
            if (tareaInsertar.getValue()) {
                System.out.println("Equipo insertado con éxito en la BD.");
                limpiarFormulario();
                cargarEquipos();
            } else {
                System.err.println("Hubo un problema al insertar el equipo.");
            }
        });

        tareaInsertar.setOnFailed(event -> {
            System.err.println("Error de conexión al intentar insertar.");
            tareaInsertar.getException().printStackTrace();
        });
        new Thread(tareaInsertar).start();
    }

    private void limpiarFormulario() {
        tfNombre.clear();
        tfCategoria.clear();
        tfPresupuesto.clear();
        dpFechaCreacion.setValue(null);
    }
}