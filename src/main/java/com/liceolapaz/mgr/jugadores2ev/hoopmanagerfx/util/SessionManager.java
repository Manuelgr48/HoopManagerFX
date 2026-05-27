package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;

public class SessionManager {
    private static SessionManager instance;

    private String usuarioLogueado;
    private String rol;
    private Integer idEquipo;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void iniciarSesion(String usuario, String rol) {
        this.usuarioLogueado = usuario;
        this.rol = rol;
        this.idEquipo = null;
    }

    public void iniciarSesion(String usuario, String rol, Integer idEquipo) {
        this.usuarioLogueado = usuario;
        this.rol = rol;
        this.idEquipo = idEquipo;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
        this.rol = null;
        this.idEquipo = null;
    }

    public String getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public String getRol() {
        return rol;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public boolean esAdmin() {
        return "ADMIN".equalsIgnoreCase(rol);
    }

    public boolean esEntrenador() {
        return "ENTRENADOR".equalsIgnoreCase(rol);
    }

    public boolean esJugador() {
        return "JUGADOR".equalsIgnoreCase(rol);
    }
}