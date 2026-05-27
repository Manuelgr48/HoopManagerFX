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

        String sql = "SELECT p.*, e.nombre AS nombre_equipo " +
                "FROM partidos p " +
                "LEFT JOIN equipos e ON p.id_equipo = e.id_equipo " +
                "ORDER BY p.fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                partidos.add(mapearPartido(rs));
            }
        }

        return partidos;
    }

    @Override
    public List<Partido> getPartidosPorEquipo(int idEquipo) throws Exception {
        List<Partido> partidos = new ArrayList<>();

        String sql = "SELECT p.*, e.nombre AS nombre_equipo " +
                "FROM partidos p " +
                "LEFT JOIN equipos e ON p.id_equipo = e.id_equipo " +
                "WHERE p.id_equipo = ? " +
                "ORDER BY p.fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEquipo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    partidos.add(mapearPartido(rs));
                }
            }
        }

        return partidos;
    }

    @Override
    public void addPartido(Partido partido) throws Exception {
        String sql = "INSERT INTO partidos (id_equipo, fecha, equipo_rival, ubicacion, resultado_propio, resultado_rival) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (partido.getIdEquipo() != null) {
                pstmt.setInt(1, partido.getIdEquipo());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }

            pstmt.setDate(2, Date.valueOf(partido.getFecha()));
            pstmt.setString(3, partido.getEquipoRival());
            pstmt.setString(4, partido.getUbicacion());
            pstmt.setInt(5, partido.getResultadoPropio());
            pstmt.setInt(6, partido.getResultadoRival());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void updatePartido(Partido partido) throws Exception {
        String sql = "UPDATE partidos SET id_equipo = ?, fecha = ?, equipo_rival = ?, ubicacion = ?, resultado_propio = ?, resultado_rival = ? WHERE id_partido = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (partido.getIdEquipo() != null) {
                pstmt.setInt(1, partido.getIdEquipo());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }

            pstmt.setDate(2, Date.valueOf(partido.getFecha()));
            pstmt.setString(3, partido.getEquipoRival());
            pstmt.setString(4, partido.getUbicacion());
            pstmt.setInt(5, partido.getResultadoPropio());
            pstmt.setInt(6, partido.getResultadoRival());
            pstmt.setInt(7, partido.getIdPartido());

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

    private Partido mapearPartido(ResultSet rs) throws Exception {
        Integer idEquipo = rs.getObject("id_equipo") != null ? rs.getInt("id_equipo") : null;

        return new Partido(
                rs.getInt("id_partido"),
                idEquipo,
                rs.getString("nombre_equipo"),
                rs.getDate("fecha").toLocalDate(),
                rs.getString("equipo_rival"),
                rs.getString("ubicacion"),
                rs.getInt("resultado_propio"),
                rs.getInt("resultado_rival")
        );
    }
}