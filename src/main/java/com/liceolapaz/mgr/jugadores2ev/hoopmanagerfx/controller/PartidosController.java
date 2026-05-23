package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service.PartidoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PartidosController implements Initializable {

    @FXML private TableView<Partido> tablaPartidos;
    @FXML private TableColumn<Partido, LocalDate> colFecha;
    @FXML private TableColumn<Partido, String> colRival;
    @FXML private TableColumn<Partido, String> colUbicacion;
    @FXML private TableColumn<Partido, Integer> colResPropio;
    @FXML private TableColumn<Partido, Integer> colResRival;

    @FXML private DatePicker dpFecha;
    @FXML private TextField tfRival;
    @FXML private TextField tfUbicacion;
    @FXML private TextField tfResPropio;
    @FXML private TextField tfResRival;

    private PartidoService partidoService;
    private ObservableList<Partido> listaPartidos;
    private Partido partidoSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partidoService = new PartidoService();
        listaPartidos = FXCollections.observableArrayList();

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colRival.setCellValueFactory(new PropertyValueFactory<>("equipoRival"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colResPropio.setCellValueFactory(new PropertyValueFactory<>("resultadoPropio"));
        colResRival.setCellValueFactory(new PropertyValueFactory<>("resultadoRival"));

        tablaPartidos.setItems(listaPartidos);

        tablaPartidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                partidoSeleccionado = newSelection;
                rellenarFormulario(partidoSeleccionado);
            }
        });

        cargarPartidos();
    }

    private void cargarPartidos() {
        Task<List<Partido>> task = new Task<>() {
            @Override
            protected List<Partido> call() throws Exception {
                return partidoService.obtenerTodosLosPartidos();
            }
        };

        task.setOnSucceeded(event -> {
            listaPartidos.setAll(task.getValue());
        });

        task.setOnFailed(event -> {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los partidos.");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    private void handleAnadir() {
        try {
            Partido p = new Partido(
                    dpFecha.getValue(),
                    tfRival.getText(),
                    tfUbicacion.getText(),
                    Integer.parseInt(tfResPropio.getText()),
                    Integer.parseInt(tfResRival.getText())
            );

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    partidoService.registrarPartido(p);
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Partido añadido correctamente.");
                cargarPartidos();
                limpiarFormulario();
            });

            task.setOnFailed(e -> mostrarAlerta(Alert.AlertType.ERROR, "Error", task.getException().getMessage()));

            new Thread(task).start();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Los resultados deben ser numéricos.");
        }
    }

    private void rellenarFormulario(Partido p) {
        dpFecha.setValue(p.getFecha());
        tfRival.setText(p.getEquipoRival());
        tfUbicacion.setText(p.getUbicacion());
        tfResPropio.setText(String.valueOf(p.getResultadoPropio()));
        tfResRival.setText(String.valueOf(p.getResultadoRival()));
    }

    @FXML
    private void limpiarFormulario() {
        dpFecha.setValue(null);
        tfRival.clear();
        tfUbicacion.clear();
        tfResPropio.clear();
        tfResRival.clear();
        partidoSeleccionado = null;
        tablaPartidos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}