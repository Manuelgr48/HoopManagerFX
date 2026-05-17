package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {

    public List<Jugador> obtenerTodos() {
        List<Jugador> jugadores = new ArrayList<>();
        String query = "SELECT * FROM jugadores";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Jugador jugador = new Jugador(
                        rs.getInt("id_jugador"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getInt("dorsal"),
                        rs.getString("posicion"),
                        rs.getDouble("altura"),
                        rs.getInt("id_equipo")
                );
                jugadores.add(jugador);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener jugadores: " + e.getMessage());
        }
        return jugadores;
    }

    public boolean insertar(Jugador jugador) {
        String query = "INSERT INTO jugadores (nombre, apellidos, dorsal, posicion, altura, id_equipo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, jugador.getNombre());
            pstmt.setString(2, jugador.getApellidos());
            pstmt.setInt(3, jugador.getDorsal());
            pstmt.setString(4, jugador.getPosicion());
            pstmt.setDouble(5, jugador.getAltura());

            if (jugador.getIdEquipo() > 0) {
                pstmt.setInt(6, jugador.getIdEquipo());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar jugador: " + e.getMessage());
            return false;
        }
    }
}