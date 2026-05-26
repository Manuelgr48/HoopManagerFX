package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaDAOImpl implements EstadisticaDAO {

    @Override
    public List<Estadistica> getAllEstadisticas() throws Exception {
        List<Estadistica> estadisticas = new ArrayList<>();
        String sql = "SELECT * FROM estadisticas";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Estadistica e = new Estadistica(
                        rs.getInt("id_estadistica"),
                        rs.getInt("id_jugador"),
                        rs.getInt("id_partido"),
                        rs.getInt("puntos"),
                        rs.getInt("rebotes"),
                        rs.getInt("asistencias"),
                        // AQUI SE HA CAMBIADO A "faltas"
                        rs.getInt("faltas")
                );
                estadisticas.add(e);
            }
        }
        return estadisticas;
    }

    @Override
    public void addEstadistica(Estadistica e) throws Exception {
        String sql = "INSERT INTO estadisticas (id_jugador, id_partido, puntos, rebotes, asistencias, faltas) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, e.getIdJugador());
            pstmt.setInt(2, e.getIdPartido());
            pstmt.setInt(3, e.getPuntos());
            pstmt.setInt(4, e.getRebotes());
            pstmt.setInt(5, e.getAsistencias());
            pstmt.setInt(6, e.getFaltasCometidas());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateEstadistica(Estadistica e) throws Exception {
        String sql = "UPDATE estadisticas SET id_jugador = ?, id_partido = ?, puntos = ?, rebotes = ?, asistencias = ?, faltas = ? WHERE id_estadistica = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, e.getIdJugador());
            pstmt.setInt(2, e.getIdPartido());
            pstmt.setInt(3, e.getPuntos());
            pstmt.setInt(4, e.getRebotes());
            pstmt.setInt(5, e.getAsistencias());
            pstmt.setInt(6, e.getFaltasCometidas());
            pstmt.setInt(7, e.getIdEstadistica());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteEstadistica(int id) throws Exception {
        String sql = "DELETE FROM estadisticas WHERE id_estadistica = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}