package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    public List<Equipo> obtenerTodos() {
        List<Equipo> equipos = new ArrayList<>();
        String query = "SELECT * FROM equipos";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Equipo equipo = new Equipo(
                        rs.getInt("id_equipo"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("presupuesto"),
                        rs.getDate("fecha_creacion").toLocalDate()
                );
                equipos.add(equipo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener equipos: " + e.getMessage());
        }
        return equipos;
    }

    public boolean insertar(Equipo equipo) {
        String query = "INSERT INTO equipos (nombre, categoria, presupuesto, fecha_creacion) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, equipo.getNombre());
            pstmt.setString(2, equipo.getCategoria());
            pstmt.setDouble(3, equipo.getPresupuesto());
            pstmt.setDate(4, Date.valueOf(equipo.getFechaCreacion()));

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar equipo: " + e.getMessage());
            return false;
        }
    }
    public boolean modificar(Equipo equipo) {
        String query = "UPDATE equipos SET nombre = ?, categoria = ?, presupuesto = ?, fecha_creacion = ? WHERE id_equipo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, equipo.getNombre());
            pstmt.setString(2, equipo.getCategoria());
            pstmt.setDouble(3, equipo.getPresupuesto());
            pstmt.setDate(4, java.sql.Date.valueOf(equipo.getFechaCreacion()));
            pstmt.setInt(5, equipo.getIdEquipo());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al modificar equipo: " + e.getMessage());
            return false;
        }
    }
    public boolean eliminar(int idEquipo) {
        String query = "DELETE FROM equipos WHERE id_equipo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idEquipo);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar equipo: " + e.getMessage());
            return false;
        }
    }
}