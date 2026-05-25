package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResumenController implements Initializable {

    @FXML
    private BarChart<String, Number> graficoPartidos;

    private PartidoService partidoService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partidoService = new PartidoService();
        cargarGrafico();
    }

    private void cargarGrafico() {
        Task<List<Partido>> task = new Task<>() {
            @Override
            protected List<Partido> call() throws Exception {
                return partidoService.obtenerTodosLosPartidos();
            }
        };

        task.setOnSucceeded(event -> {
            List<Partido> partidos = task.getValue();

            XYChart.Series<String, Number> serieFavor = new XYChart.Series<>();
            serieFavor.setName("Puntos a Favor");

            XYChart.Series<String, Number> serieContra = new XYChart.Series<>();
            serieContra.setName("Puntos en Contra");

            for (Partido p : partidos) {
                serieFavor.getData().add(new XYChart.Data<>(p.getEquipoRival(), p.getResultadoPropio()));
                serieContra.getData().add(new XYChart.Data<>(p.getEquipoRival(), p.getResultadoRival()));
            }

            Platform.runLater(() -> {
                graficoPartidos.getData().clear();
                graficoPartidos.getData().addAll(serieFavor, serieContra);
            });
        });

        task.setOnFailed(event -> {
            System.err.println("Error al cargar los datos del gráfico.");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}