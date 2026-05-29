package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAOImpl;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PartidoInfoController {

    @FXML private Label lblTitulo;
    @FXML private Label lblResultado;
    @FXML private Label lblEquipo;

    @FXML private TableView<Estadistica> tablaEstadisticas;
    @FXML private TableColumn<Estadistica, String> colJugador;
    @FXML private TableColumn<Estadistica, Integer> colPuntos;
    @FXML private TableColumn<Estadistica, Integer> colRebotes;
    @FXML private TableColumn<Estadistica, Integer> colAsistencias;
    @FXML private TableColumn<Estadistica, Integer> colFaltas;

    private final EstadisticaDAO estadisticaDAO = new EstadisticaDAOImpl();
    private final ObservableList<Estadistica> estadisticas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colJugador.setCellValueFactory(new PropertyValueFactory<>("nombreJugador"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colRebotes.setCellValueFactory(new PropertyValueFactory<>("rebotes"));
        colAsistencias.setCellValueFactory(new PropertyValueFactory<>("asistencias"));
        colFaltas.setCellValueFactory(new PropertyValueFactory<>("faltasCometidas"));

        tablaEstadisticas.setItems(estadisticas);
    }

    public void configurar(Partido partido) {
        lblTitulo.setText(partido.getNombreEquipo() + " vs " + partido.getEquipoRival() + " - " + partido.getFecha());
        lblResultado.setText(partido.getResultadoPropio() + " - " + partido.getResultadoRival());
        lblEquipo.setText("Estadisticas de " + partido.getNombreEquipo());

        estadisticas.clear();

        if (partido.getIdEquipo() == null) {
            tablaEstadisticas.setPlaceholder(new Label("Este partido no tiene equipo asignado. Editalo y selecciona el equipo local."));
            return;
        }

        for (Estadistica estadistica : estadisticaDAO.obtenerPorPartido(partido.getIdPartido())) {
            if (estadistica.getIdEquipoJugador() != null
                    && estadistica.getIdEquipoJugador().equals(partido.getIdEquipo())) {
                estadisticas.add(estadistica);
            }
        }

        if (estadisticas.isEmpty()) {
            tablaEstadisticas.setPlaceholder(new Label("No hay estadisticas registradas para jugadores de este equipo en este partido."));
        }
    }
}