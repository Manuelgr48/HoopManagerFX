package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    @FXML private Button btnAnadir;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    private JugadorDAO jugadorDAO;
    private ObservableList<Jugador> listaJugadores;

    public JugadoresController() {
        this.jugadorDAO = new JugadorDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaJugadores = FXCollections.observableArrayList();

        this.colId.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        this.colDorsal.setCellValueFactory(new PropertyValueFactory<>("dorsal"));
        this.colPosicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        this.colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        this.colEquipo.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));

        cargarJugadores();
        configurarPermisos();

        FilteredList<Jugador> filteredData = new FilteredList<>(listaJugadores, p -> true);

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

        SortedList<Jugador> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaJugadores.comparatorProperty());
        tablaJugadores.setItems(sortedData);
    }

    private void cargarJugadores() {
        listaJugadores.clear();
        listaJugadores.addAll(jugadorDAO.obtenerTodos());
    }

    private void configurarPermisos() {
        String rolActual = SessionManager.getInstance().getRol();
        Integer equipoEntrenador = SessionManager.getInstance().getIdEquipo();

        if (btnAnadir == null) return;

        if ("JUGADOR".equals(rolActual)) {
            btnAnadir.setVisible(false);
            btnModificar.setVisible(false);
            btnEliminar.setVisible(false);
        } else if ("ENTRENADOR".equals(rolActual)) {
            btnAnadir.setVisible(true);
            tablaJugadores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    boolean esDeSuEquipo = equipoEntrenador != null && (newSel.getIdEquipo() == equipoEntrenador);
                    btnModificar.setDisable(!esDeSuEquipo);
                    btnEliminar.setDisable(!esDeSuEquipo);
                }
            });
        } else if ("ADMIN".equals(rolActual)) {
            // El admin todo lo puede
            btnAnadir.setVisible(true);
            btnModificar.setVisible(true);
            btnEliminar.setVisible(true);
        }
    }
}