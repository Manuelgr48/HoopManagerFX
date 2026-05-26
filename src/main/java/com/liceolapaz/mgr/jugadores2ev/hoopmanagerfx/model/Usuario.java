package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

public class Usuario {
    private int idUsuario;
    private String username;
    private String password;
    private String rol;

    public Usuario(int idUsuario, String username, String password, String rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getUsername() { return username; }
    public String getRol() { return rol; }
}