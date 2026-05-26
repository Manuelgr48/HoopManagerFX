package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

public class Usuario {
    private int idUsuario;
    private String username;
    private String password;
    private String rol;
    private Integer idEquipo;

    public Usuario(int idUsuario, String username, String password, String rol, Integer idEquipo) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.idEquipo = this.idEquipo;
    }
    public int getIdUsuario() { return idUsuario; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public Integer getIdEquipo() { return idEquipo; }
    public void setIdEquipo(Integer idEquipo) { this.idEquipo = idEquipo; }
}