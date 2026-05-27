package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Usuario;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario autenticarUsuario(String correo, String password) {
        String sql = "SELECT u.*, e.nombre AS nombre_equipo " +
                "FROM usuarios u LEFT JOIN equipos e ON u.id_equipo = e.id_equipo " +
                "WHERE u.correo = ? AND u.password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            pstmt.setString(2, PasswordHasher.hashPassword(password));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, e.nombre AS nombre_equipo " +
                "FROM usuarios u LEFT JOIN equipos e ON u.id_equipo = e.id_equipo " +
                "ORDER BY u.id_usuario";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public List<Usuario> buscar(String filtro) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, e.nombre AS nombre_equipo " +
                "FROM usuarios u LEFT JOIN equipos e ON u.id_equipo = e.id_equipo " +
                "WHERE LOWER(u.nombre) LIKE ? OR LOWER(u.apellidos) LIKE ? OR LOWER(u.correo) LIKE ? " +
                "OR LOWER(u.rol) LIKE ? OR LOWER(e.nombre) LIKE ? ORDER BY u.id_usuario";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String patron = "%" + filtro.toLowerCase() + "%";
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, patron);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public int contarAdmins() {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE rol = 'ADMIN'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean esUltimoAdmin(int idUsuario) {
        String sql = "SELECT rol FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "ADMIN".equals(rs.getString("rol")) && contarAdmins() <= 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean registrarUsuario(String nombre, String apellidos, String correo, String password, String rol, Integer idEquipo) {
        String sql = "INSERT INTO usuarios (nombre, apellidos, correo, password, rol, id_equipo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, correo);
            pstmt.setString(4, PasswordHasher.hashPassword(password));
            pstmt.setString(5, rol);

            if (idEquipo != null) {
                pstmt.setInt(6, idEquipo);
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarUsuario(int idUsuario, String nombre, String apellidos, String correo, String password, String rol, Integer idEquipo) {
        if (!"ADMIN".equals(rol) && esUltimoAdmin(idUsuario)) {
            return false;
        }

        boolean cambiarPassword = password != null && !password.trim().isEmpty();

        String sql = cambiarPassword
                ? "UPDATE usuarios SET nombre = ?, apellidos = ?, correo = ?, password = ?, rol = ?, id_equipo = ? WHERE id_usuario = ?"
                : "UPDATE usuarios SET nombre = ?, apellidos = ?, correo = ?, rol = ?, id_equipo = ? WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, correo);

            if (cambiarPassword) {
                pstmt.setString(4, PasswordHasher.hashPassword(password));
                pstmt.setString(5, rol);
                setNullableEquipo(pstmt, 6, idEquipo);
                pstmt.setInt(7, idUsuario);
            } else {
                pstmt.setString(4, rol);
                setNullableEquipo(pstmt, 5, idEquipo);
                pstmt.setInt(6, idUsuario);
            }

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        if (esUltimoAdmin(idUsuario)) {
            return false;
        }

        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setNullableEquipo(PreparedStatement pstmt, int index, Integer idEquipo) throws Exception {
        if (idEquipo != null) {
            pstmt.setInt(index, idEquipo);
        } else {
            pstmt.setNull(index, java.sql.Types.INTEGER);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws Exception {
        Integer idEquipo = rs.getObject("id_equipo") != null ? rs.getInt("id_equipo") : null;

        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("correo"),
                rs.getString("password"),
                rs.getString("rol"),
                idEquipo,
                rs.getString("nombre_equipo")
        );
    }
}