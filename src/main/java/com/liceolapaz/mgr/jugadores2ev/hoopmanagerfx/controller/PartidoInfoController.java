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

    @FXML private TableView<Estadistica> tablaPropios;
    @FXML private TableColumn<Estadistica, String> colJugadorPropio;
    @FXML private TableColumn<Estadistica, Integer> colPuntosPropio;
    @FXML private TableColumn<Estadistica, Integer> colRebotesPropio;
    @FXML private TableColumn<Estadistica, Integer> colAsistenciasPropio;
    @FXML private TableColumn<Estadistica, Integer> colFaltasPropio;

    @FXML private TableView<Estadistica> tablaRivales;
    @FXML private TableColumn<Estadistica, String> colJugadorRival;
    @FXML private TableColumn<Estadistica, Integer> colPuntosRival;
    @FXML private TableColumn<Estadistica, Integer> colRebotesRival;
    @FXML private TableColumn<Estadistica, Integer> colAsistenciasRival;
    @FXML private TableColumn<Estadistica, Integer> colFaltasRival;

    private final EstadisticaDAO estadisticaDAO = new EstadisticaDAOImpl();
    private final ObservableList<Estadistica> propios = FXCollections.observableArrayList();
    private final ObservableList<Estadistica> rivales = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColumnas(colJugadorPropio, colPuntosPropio, colRebotesPropio, colAsistenciasPropio, colFaltasPropio);
        configurarColumnas(colJugadorRival, colPuntosRival, colRebotesRival, colAsistenciasRival, colFaltasRival);

        tablaPropios.setItems(propios);
        tablaRivales.setItems(rivales);
    }

    public void configurar(Partido partido) {
        lblTitulo.setText(partido.getNombreEquipo() + " vs " + partido.getEquipoRival() + " - " + partido.getFecha());
        lblResultado.setText(partido.getResultadoPropio() + " - " + partido.getResultadoRival());

        propios.clear();
        rivales.clear();

        for (Estadistica estadistica : estadisticaDAO.obtenerPorPartido(partido.getIdPartido())) {
            if (estadistica.getIdEquipoJugador() != null
                    && partido.getIdEquipo() != null
                    && estadistica.getIdEquipoJugador().equals(partido.getIdEquipo())) {
                propios.add(estadistica);
            } else {
                rivales.add(estadistica);
            }
        }
    }

    private void configurarColumnas(TableColumn<Estadistica, String> jugador,
                                    TableColumn<Estadistica, Integer> puntos,
                                    TableColumn<Estadistica, Integer> rebotes,
                                    TableColumn<Estadistica, Integer> asistencias,
                                    TableColumn<Estadistica, Integer> faltas) {
        jugador.setCellValueFactory(new PropertyValueFactory<>("nombreJugador"));
        puntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        rebotes.setCellValueFactory(new PropertyValueFactory<>("rebotes"));
        asistencias.setCellValueFactory(new PropertyValueFactory<>("asistencias"));
        faltas.setCellValueFactory(new PropertyValueFactory<>("faltasCometidas"));
    }
}