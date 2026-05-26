package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private String correo;
    private String password;
    private String rol;
    private Integer idEquipo;

    public Usuario(int idUsuario, String nombre, String apellidos, String correo, String password, String rol, Integer idEquipo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.idEquipo = idEquipo;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public Integer getIdEquipo() { return idEquipo; }
    public void setIdEquipo(Integer idEquipo) { this.idEquipo = idEquipo; }
}