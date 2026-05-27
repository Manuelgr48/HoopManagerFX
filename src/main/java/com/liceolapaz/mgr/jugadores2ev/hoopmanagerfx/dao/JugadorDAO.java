package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {

    public List<Jugador> obtenerTodos() {
        List<Jugador> jugadores = new ArrayList<>();

        String sql = "SELECT j.*, e.nombre AS nombre_equipo " +
                "FROM jugadores j " +
                "LEFT JOIN equipos e ON j.id_equipo = e.id_equipo " +
                "ORDER BY e.nombre, j.apellidos, j.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                jugadores.add(mapearJugador(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jugadores;
    }

    public List<Jugador> obtenerPorEquipo(int idEquipo) {
        List<Jugador> jugadores = new ArrayList<>();

        String sql = "SELECT j.*, e.nombre AS nombre_equipo " +
                "FROM jugadores j " +
                "LEFT JOIN equipos e ON j.id_equipo = e.id_equipo " +
                "WHERE j.id_equipo = ? " +
                "ORDER BY j.apellidos, j.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEquipo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jugadores.add(mapearJugador(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jugadores;
    }

    public boolean insertar(Jugador jugador) {
        String sql = "INSERT INTO jugadores (nombre, apellidos, dorsal, posicion, altura, id_equipo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, jugador.getNombre());
            pstmt.setString(2, jugador.getApellidos());
            pstmt.setInt(3, jugador.getDorsal());
            pstmt.setString(4, jugador.getPosicion());
            pstmt.setDouble(5, jugador.getAltura());

            if (jugador.getIdEquipo() != null) {
                pstmt.setInt(6, jugador.getIdEquipo());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Jugador jugador) {
        String sql = "UPDATE jugadores SET nombre = ?, apellidos = ?, dorsal = ?, posicion = ?, altura = ?, id_equipo = ? WHERE id_jugador = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, jugador.getNombre());
            pstmt.setString(2, jugador.getApellidos());
            pstmt.setInt(3, jugador.getDorsal());
            pstmt.setString(4, jugador.getPosicion());
            pstmt.setDouble(5, jugador.getAltura());

            if (jugador.getIdEquipo() != null) {
                pstmt.setInt(6, jugador.getIdEquipo());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            pstmt.setInt(7, jugador.getIdJugador());

            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM jugadores WHERE id_jugador = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer obtenerIdPorNombre(String textoBusqueda) {
        String sql = "SELECT id_jugador FROM jugadores WHERE CONCAT(nombre, ' ', apellidos) = ? OR nombre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, textoBusqueda);
            pstmt.setString(2, textoBusqueda);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_jugador");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Jugador mapearJugador(ResultSet rs) throws Exception {
        Integer idEquipo = rs.getObject("id_equipo") != null ? rs.getInt("id_equipo") : null;

        return new Jugador(
                rs.getInt("id_jugador"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getInt("dorsal"),
                rs.getString("posicion"),
                rs.getDouble("altura"),
                idEquipo,
                rs.getString("nombre_equipo")
        );
    }
}