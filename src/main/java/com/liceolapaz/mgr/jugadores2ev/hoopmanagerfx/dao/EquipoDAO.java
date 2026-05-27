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
        String sql = "SELECT * FROM equipos ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                equipos.add(mapearEquipo(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return equipos;
    }

    public List<Equipo> buscarPorNombreOCategoria(String filtro) {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT * FROM equipos WHERE LOWER(nombre) LIKE ? OR LOWER(categoria) LIKE ? ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String patron = "%" + filtro.toLowerCase() + "%";
            pstmt.setString(1, patron);
            pstmt.setString(2, patron);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    equipos.add(mapearEquipo(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return equipos;
    }

    public Equipo obtenerPorId(int idEquipo) {
        String sql = "SELECT * FROM equipos WHERE id_equipo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEquipo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEquipo(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

    public boolean eliminar(int idEquipo) {
        String sql = "DELETE FROM equipos WHERE id_equipo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEquipo);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Equipo mapearEquipo(ResultSet rs) throws Exception {
        return new Equipo(
                rs.getInt("id_equipo"),
                rs.getString("nombre"),
                rs.getString("categoria"),
                rs.getDouble("presupuesto"),
                rs.getDate("fecha_creacion").toLocalDate()
        );
    }
}