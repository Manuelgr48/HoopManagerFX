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
        String sql = "SELECT j.*, e.nombre AS nombre_equipo FROM jugadores j LEFT JOIN equipos e ON j.id_equipo = e.id_equipo";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Integer idEquipo = rs.getObject("id_equipo") != null ? rs.getInt("id_equipo") : null;
                String nombreEquipo = rs.getString("nombre_equipo");

                Jugador jugador = new Jugador(
                        rs.getInt("id_jugador"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getInt("dorsal"),
                        rs.getString("posicion"),
                        rs.getDouble("altura"),
                        idEquipo,
                        nombreEquipo
                );
                jugadores.add(jugador);
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

            if (jugador.getIdEquipo() != null) pstmt.setInt(6, jugador.getIdEquipo());
            else pstmt.setNull(6, java.sql.Types.INTEGER);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
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

            if (jugador.getIdEquipo() != null) pstmt.setInt(6, jugador.getIdEquipo());
            else pstmt.setNull(6, java.sql.Types.INTEGER);

            pstmt.setInt(7, jugador.getIdJugador());

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM jugadores WHERE id_jugador = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}