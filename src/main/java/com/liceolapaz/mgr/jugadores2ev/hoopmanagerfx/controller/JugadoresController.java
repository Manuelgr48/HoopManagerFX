package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class JugadoresController implements Initializable {

    @FXML private TableView<Jugador> tablaJugadores;
    @FXML private TableColumn<Jugador, Integer> colId;
    @FXML private TableColumn<Jugador, String> colNombre;
    @FXML private TableColumn<Jugador, String> colApellidos;
    @FXML private TableColumn<Jugador, Integer> colDorsal;
    @FXML private TableColumn<Jugador, String> colPosicion;
    @FXML private TableColumn<Jugador, Double> colAltura;
    @FXML private TableColumn<Jugador, Integer> colEquipo;
    @FXML private TextField tfBuscador;

    private JugadorDAO jugadorDAO;
    private ObservableList<Jugador> listaJugadores;

    public JugadoresController() {
        this.jugadorDAO = new JugadorDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaJugadores = FXCollections.observableArrayList();

        this.colId.setCellValueFactory(new PropertyValueFactory<>("id_jugador"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        this.colDorsal.setCellValueFactory(new PropertyValueFactory<>("dorsal"));
        this.colPosicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        this.colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        this.colEquipo.setCellValueFactory(new PropertyValueFactory<>("id_equipo"));

        cargarJugadores();
        javafx.collections.transformation.FilteredList<com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador> filteredData =
                new javafx.collections.transformation.FilteredList<>(listaJugadores, p -> true);

        tfBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(jugador -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (jugador.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (jugador.getApellidos().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        javafx.collections.transformation.SortedList<com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador> sortedData =
                new javafx.collections.transformation.SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaJugadores.comparatorProperty());

        tablaJugadores.setItems(sortedData);
    }

    private void cargarJugadores() {
        listaJugadores.clear();
        tablaJugadores.setItems(listaJugadores);
    }
}