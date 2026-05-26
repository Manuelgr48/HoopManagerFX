package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.ReporteService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResumenController implements Initializable {

    @FXML
    private void handleExportarPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Informe PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        fileChooser.setInitialFileName("Informe_HoopManager.pdf");

        File file = fileChooser.showSaveDialog(graficoPartidos.getScene().getWindow());

        if (file != null) {
            try {
                ReporteService reporteService = new ReporteService();
                reporteService.generarInformeJugadores(file.getAbsolutePath());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("El informe ha sido generado correctamente.");
                alert.showAndWait();

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Fallo al generar el PDF");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

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