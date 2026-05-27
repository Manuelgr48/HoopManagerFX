package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaDAOImpl implements EstadisticaDAO {

    @Override
    public List<Estadistica> obtenerTodas() {
        List<Estadistica> estadisticas = new ArrayList<>();

        String sql = "SELECT e.*, CONCAT(j.nombre, ' ', j.apellidos) AS nombre_completo, p.equipo_rival " +
                "FROM estadisticas e " +
                "LEFT JOIN jugadores j ON e.id_jugador = j.id_jugador " +
                "LEFT JOIN partidos p ON e.id_partido = p.id_partido";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                estadisticas.add(mapearEstadistica(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return estadisticas;
    }

    @Override
    public List<Estadistica> obtenerPorJugador(int idJugador) {
        List<Estadistica> estadisticas = new ArrayList<>();

        String sql = "SELECT e.*, CONCAT(j.nombre, ' ', j.apellidos) AS nombre_completo, p.equipo_rival " +
                "FROM estadisticas e " +
                "LEFT JOIN jugadores j ON e.id_jugador = j.id_jugador " +
                "LEFT JOIN partidos p ON e.id_partido = p.id_partido " +
                "WHERE e.id_jugador = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idJugador);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    estadisticas.add(mapearEstadistica(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return estadisticas;
    }

    @Override
    public boolean insertar(Estadistica estadistica) {
        String sql = "INSERT INTO estadisticas (id_jugador, id_partido, puntos, rebotes, asistencias, faltas_cometidas) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, estadistica.getIdJugador());
            pstmt.setInt(2, estadistica.getIdPartido());
            pstmt.setInt(3, estadistica.getPuntos());
            pstmt.setInt(4, estadistica.getRebotes());
            pstmt.setInt(5, estadistica.getAsistencias());
            pstmt.setInt(6, estadistica.getFaltasCometidas());

            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Estadistica estadistica) {
        String sql = "UPDATE estadisticas SET id_jugador = ?, id_partido = ?, puntos = ?, rebotes = ?, asistencias = ?, faltas_cometidas = ? WHERE id_estadistica = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, estadistica.getIdJugador());
            pstmt.setInt(2, estadistica.getIdPartido());
            pstmt.setInt(3, estadistica.getPuntos());
            pstmt.setInt(4, estadistica.getRebotes());
            pstmt.setInt(5, estadistica.getAsistencias());
            pstmt.setInt(6, estadistica.getFaltasCometidas());
            pstmt.setInt(7, estadistica.getIdEstadistica());

            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estadisticas WHERE id_estadistica = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Estadistica mapearEstadistica(ResultSet rs) throws Exception {
        String nombreJugador = rs.getString("nombre_completo");
        if (nombreJugador == null) {
            nombreJugador = "Jugador desconocido";
        }

        return new Estadistica(
                rs.getInt("id_estadistica"),
                rs.getInt("id_jugador"),
                nombreJugador,
                rs.getInt("id_partido"),
                rs.getString("equipo_rival"),
                rs.getInt("puntos"),
                rs.getInt("rebotes"),
                rs.getInt("asistencias"),
                rs.getInt("faltas_cometidas")
        );
    }

    @Override
    public void addEstadistica(Estadistica estadistica) throws Exception {
        insertar(estadistica);
    }

    @Override
    public void updateEstadistica(Estadistica estadistica) throws Exception {
        actualizar(estadistica);
    }

    @Override
    public void deleteEstadistica(int id) throws Exception {
        eliminar(id);
    }

    @Override
    public List<Estadistica> getAllEstadisticas() throws Exception {
        return obtenerTodas();
    }
}