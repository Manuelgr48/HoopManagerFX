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
        String passwordHasheada = PasswordHasher.hashPassword(password);
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, passwordHasheada);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Integer idEquipo = rs.getInt("id_equipo");
                    if (rs.wasNull()) idEquipo = null;
                    return new Usuario(
                            rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("apellidos"),
                            rs.getString("correo"), rs.getString("password"), rs.getString("rol"), idEquipo
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean registrarUsuario(String nombre, String apellidos, String correo, String password, String rol, Integer idEquipo) {
        String passwordHasheada = PasswordHasher.hashPassword(password);
        String sql = "INSERT INTO usuarios (nombre, apellidos, correo, password, rol, id_equipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, correo);
            pstmt.setString(4, passwordHasheada);
            pstmt.setString(5, rol);
            if (idEquipo != null) pstmt.setInt(6, idEquipo);
            else pstmt.setNull(6, java.sql.Types.INTEGER);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Integer idEquipo = rs.getInt("id_equipo");
                if (rs.wasNull()) idEquipo = null;
                usuarios.add(new Usuario(
                        rs.getInt("id_usuario"), rs.getString("nombre"), rs.getString("apellidos"),
                        rs.getString("correo"), rs.getString("password"), rs.getString("rol"), idEquipo
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return usuarios;
    }

    public boolean actualizarRolYEquipo(int idUsuario, String nuevoRol, Integer nuevoIdEquipo) {
        String sql = "UPDATE usuarios SET rol = ?, id_equipo = ? WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoRol);
            if (nuevoIdEquipo != null) pstmt.setInt(2, nuevoIdEquipo);
            else pstmt.setNull(2, java.sql.Types.INTEGER);
            pstmt.setInt(3, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}