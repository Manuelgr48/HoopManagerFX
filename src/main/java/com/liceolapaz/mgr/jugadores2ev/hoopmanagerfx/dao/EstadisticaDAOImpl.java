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
        String sql = "SELECT e.*, CONCAT(j.nombre, ' ', j.apellidos) AS nombre_completo " +
                "FROM estadisticas e " +
                "LEFT JOIN jugadores j ON e.id_jugador = j.id_jugador";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nombreJugador = rs.getString("nombre_completo");
                if (nombreJugador == null) nombreJugador = "Jugador Desconocido";

                Estadistica est = new Estadistica(
                        rs.getInt("id_estadistica"),
                        rs.getInt("id_jugador"),
                        nombreJugador,
                        rs.getInt("id_partido"),
                        rs.getInt("puntos"),
                        rs.getInt("rebotes"),
                        rs.getInt("asistencias"),
                        rs.getInt("faltas_cometidas")
                );
                estadisticas.add(est);
            }
        } catch (Exception e) { e.printStackTrace(); }
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
        String sql = "UPDATE estadisticas SET id_jugador=?, id_partido=?, puntos=?, rebotes=?, asistencias=?, faltas_cometidas=? WHERE id_estadistica=?";
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
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estadisticas WHERE id_estadistica=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    @Override
    public void addEstadistica(Estadistica estadistica) throws Exception {

    }

    @Override
    public void updateEstadistica(Estadistica estadistica) throws Exception {

    }

    @Override
    public void deleteEstadistica(int id) throws Exception {

    }

    @Override
    public List<Estadistica> getAllEstadisticas() throws Exception {
        return List.of();
    }
}