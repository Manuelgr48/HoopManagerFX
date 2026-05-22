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
        listaEquipos.clear();
        tablaEquipos.setItems(listaEquipos);
    }
}