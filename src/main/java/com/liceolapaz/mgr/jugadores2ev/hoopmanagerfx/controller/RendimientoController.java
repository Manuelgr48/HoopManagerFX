package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EquipoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.List;

public class RendimientoController {

    @FXML private ComboBox<Equipo> cbEquipo;
    @FXML private BarChart<String, Number> graficoRendimiento;
    @FXML private Label lblResumen;

    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final PartidoService partidoService = new PartidoService();

    @FXML
    public void initialize() {
        cargarEquipos();
        cargarRendimientoGeneral();

        cbEquipo.setOnAction(event -> {
            Equipo equipo = cbEquipo.getValue();

            if (equipo != null) {
                cargarRendimientoEquipo(equipo);
            }
        });
    }

    private void cargarEquipos() {
        cbEquipo.setItems(FXCollections.observableArrayList(equipoDAO.obtenerTodos()));
    }

    @FXML
    private void cargarRendimientoGeneral() {
        cbEquipo.getSelectionModel().clearSelection();
        List<Partido> partidos = partidoService.obtenerTodosLosPartidos();
        pintarGrafico(partidos, "Rendimiento general");
    }

    private void cargarRendimientoEquipo(Equipo equipo) {
        List<Partido> partidos = partidoService.obtenerPartidosPorEquipo(equipo.getIdEquipo());
        pintarGrafico(partidos, "Rendimiento de " + equipo.getNombre());
    }

    private void pintarGrafico(List<Partido> partidos, String titulo) {
        graficoRendimiento.getData().clear();
        graficoRendimiento.setTitle(titulo);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();

        int victorias = 0;
        int derrotas = 0;
        int empates = 0;

        for (Partido partido : partidos) {
            int diferencia = partido.getResultadoPropio() - partido.getResultadoRival();

            if (diferencia > 0) {
                victorias++;
            } else if (diferencia < 0) {
                derrotas++;
            } else {
                empates++;
            }

            String etiqueta = partido.getNombreEquipo() + " vs " + partido.getEquipoRival();
            serie.getData().add(new XYChart.Data<>(etiqueta, diferencia));
        }

        graficoRendimiento.getData().add(serie);

        lblResumen.setText(
                "Partidos: " + partidos.size()
                        + " | Victorias: " + victorias
                        + " | Derrotas: " + derrotas
                        + " | Empates: " + empates
        );
    }
}