package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.EstadisticaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EstadisticaController implements Initializable {

    @FXML private TableView<Estadistica> tablaEstadisticas;
    @FXML private TableColumn<Estadistica, Integer> colIdJugador;
    @FXML private TableColumn<Estadistica, Integer> colIdPartido;
    @FXML private TableColumn<Estadistica, Integer> colPuntos;
    @FXML private TableColumn<Estadistica, Integer> colRebotes;
    @FXML private TableColumn<Estadistica, Integer> colAsistencias;
    @FXML private TableColumn<Estadistica, Integer> colFaltas;

    @FXML private TextField tfIdJugador;
    @FXML private TextField tfIdPartido;
    @FXML private TextField tfPuntos;
    @FXML private TextField tfRebotes;
    @FXML private TextField tfAsistencias;
    @FXML private TextField tfFaltas;

    private EstadisticaService estadisticaService;
    private ObservableList<Estadistica> listaEstadisticas;
    private Estadistica estadisticaSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        estadisticaService = new EstadisticaService();
        listaEstadisticas = FXCollections.observableArrayList();

        colIdJugador.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
        colIdPartido.setCellValueFactory(new PropertyValueFactory<>("idPartido"));
        colPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        colRebotes.setCellValueFactory(new PropertyValueFactory<>("rebotes"));
        colAsistencias.setCellValueFactory(new PropertyValueFactory<>("asistencias"));
        colFaltas.setCellValueFactory(new PropertyValueFactory<>("faltasCometidas"));

        tablaEstadisticas.setItems(listaEstadisticas);

        tablaEstadisticas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                estadisticaSeleccionada = newSelection;
                rellenarFormulario(estadisticaSeleccionada);
            }
        });

        cargarEstadisticas();
    }

    private void cargarEstadisticas() {
        Task<List<Estadistica>> task = new Task<>() {
            @Override
            protected List<Estadistica> call() throws Exception {
                return estadisticaService.obtenerTodasLasEstadisticas();
            }
        };

        task.setOnSucceeded(event -> {
            listaEstadisticas.setAll(task.getValue());
        });

        task.setOnFailed(event -> {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las estadísticas.");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    private void handleAnadir() {
        try {
            Estadistica e = new Estadistica(
                    Integer.parseInt(tfIdJugador.getText()),
                    Integer.parseInt(tfIdPartido.getText()),
                    Integer.parseInt(tfPuntos.getText()),
                    Integer.parseInt(tfRebotes.getText()),
                    Integer.parseInt(tfAsistencias.getText()),
                    Integer.parseInt(tfFaltas.getText())
            );

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    estadisticaService.registrarEstadistica(e);
                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estadística añadida correctamente.");
                cargarEstadisticas();
                limpiarFormulario();
            });

            task.setOnFailed(event -> mostrarAlerta(Alert.AlertType.ERROR, "Error", task.getException().getMessage()));

            new Thread(task).start();

        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Asegúrate de que todos los campos sean números enteros.");
        }
    }

    @FXML
    private void handleModificar() {
        if (estadisticaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Por favor, selecciona una estadística de la tabla.");
            return;
        }

        try {
            estadisticaSeleccionada.setIdJugador(Integer.parseInt(tfIdJugador.getText()));
            estadisticaSeleccionada.setIdPartido(Integer.parseInt(tfIdPartido.getText()));
            estadisticaSeleccionada.setPuntos(Integer.parseInt(tfPuntos.getText()));
            estadisticaSeleccionada.setRebotes(Integer.parseInt(tfRebotes.getText()));
            estadisticaSeleccionada.setAsistencias(Integer.parseInt(tfAsistencias.getText()));
            estadisticaSeleccionada.setFaltasCometidas(Integer.parseInt(tfFaltas.getText()));

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    estadisticaService.actualizarEstadistica(estadisticaSeleccionada);
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estadística actualizada.");
                tablaEstadisticas.refresh();
                limpiarFormulario();
            });

            task.setOnFailed(e -> mostrarAlerta(Alert.AlertType.ERROR, "Error", task.getException().getMessage()));

            new Thread(task).start();

        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Asegúrate de que todos los campos sean números enteros.");
        }
    }

    @FXML
    private void handleEliminar() {
        if (estadisticaSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección", "Selecciona una estadística para eliminar.");
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                estadisticaService.eliminarEstadistica(estadisticaSeleccionada.getIdEstadistica());
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            listaEstadisticas.remove(estadisticaSeleccionada);
            limpiarFormulario();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Estadística eliminada.");
        });

        task.setOnFailed(e -> mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar la estadística."));

        new Thread(task).start();
    }

    private void rellenarFormulario(Estadistica e) {
        tfIdJugador.setText(String.valueOf(e.getIdJugador()));
        tfIdPartido.setText(String.valueOf(e.getIdPartido()));
        tfPuntos.setText(String.valueOf(e.getPuntos()));
        tfRebotes.setText(String.valueOf(e.getRebotes()));
        tfAsistencias.setText(String.valueOf(e.getAsistencias()));
        tfFaltas.setText(String.valueOf(e.getFaltasCometidas()));
    }

    @FXML
    private void limpiarFormulario() {
        tfIdJugador.clear();
        tfIdPartido.clear();
        tfPuntos.clear();
        tfRebotes.clear();
        tfAsistencias.clear();
        tfFaltas.clear();
        estadisticaSeleccionada = null;
        tablaEstadisticas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}