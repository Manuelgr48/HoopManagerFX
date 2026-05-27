package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    public List<Equipo> obtenerTodos() {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT * FROM equipos";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return equipos;
    }

    public Integer obtenerIdPorNombre(String nombreEquipo) {
        String sql = "SELECT id_equipo FROM equipos WHERE nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreEquipo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_equipo");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertar(Equipo equipo) {
        String sql = "INSERT INTO equipos (nombre, categoria, presupuesto, fecha_creacion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, equipo.getNombre());
            pstmt.setString(2, equipo.getCategoria());
            pstmt.setDouble(3, equipo.getPresupuesto());
            pstmt.setDate(4, java.sql.Date.valueOf(equipo.getFechaCreacion()));
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modificar(Equipo equipo) {
        String sql = "UPDATE equipos SET nombre = ?, categoria = ?, presupuesto = ?, fecha_creacion = ? WHERE id_equipo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, equipo.getNombre());
            pstmt.setString(2, equipo.getCategoria());
            pstmt.setDouble(3, equipo.getPresupuesto());
            pstmt.setDate(4, java.sql.Date.valueOf(equipo.getFechaCreacion()));
            pstmt.setInt(5, equipo.getIdEquipo());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM equipos WHERE id_equipo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}