package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidoDAOImpl implements PartidoDAO {

    @Override
    public List<Partido> getAllPartidos() throws Exception {
        List<Partido> partidos = new ArrayList<>();
        String sql = "SELECT * FROM partidos";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Partido p = new Partido(
                        rs.getInt("id_partido"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("equipo_rival"),
                        rs.getString("ubicacion"),
                        rs.getInt("resultado_propio"),
                        rs.getInt("resultado_rival")
                );
                partidos.add(p);
            }
        }
        return partidos;
    }

    @Override
    public void addPartido(Partido partido) throws Exception {
        String sql = "INSERT INTO partidos (fecha, equipo_rival, ubicacion, resultado_propio, resultado_rival) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(partido.getFecha()));
            pstmt.setString(2, partido.getEquipoRival());
            pstmt.setString(3, partido.getUbicacion());
            pstmt.setInt(4, partido.getResultadoPropio());
            pstmt.setInt(5, partido.getResultadoRival());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updatePartido(Partido partido) throws Exception {
        String sql = "UPDATE partidos SET fecha = ?, equipo_rival = ?, ubicacion = ?, resultado_propio = ?, resultado_rival = ? WHERE id_partido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(partido.getFecha()));
            pstmt.setString(2, partido.getEquipoRival());
            pstmt.setString(3, partido.getUbicacion());
            pstmt.setInt(4, partido.getResultadoPropio());
            pstmt.setInt(5, partido.getResultadoRival());
            pstmt.setInt(6, partido.getIdPartido());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deletePartido(int id) throws Exception {
        String sql = "DELETE FROM partidos WHERE id_partido = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}