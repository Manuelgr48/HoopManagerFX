package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

public class Estadistica {
    private int idEstadistica;
    private int idJugador;
    private String nombreJugador;
    private int idPartido;
    private int puntos;
    private int rebotes;
    private int asistencias;
    private int faltasCometidas;

    public Estadistica(int idEstadistica, int idJugador, String nombreJugador, int idPartido, int puntos, int rebotes, int asistencias, int faltasCometidas) {
        this.idEstadistica = idEstadistica;
        this.idJugador = idJugador;
        this.nombreJugador = nombreJugador;
        this.idPartido = idPartido;
        this.puntos = puntos;
        this.rebotes = rebotes;
        this.asistencias = asistencias;
        this.faltasCometidas = faltasCometidas;
    }

    public int getIdEstadistica() { return idEstadistica; }
    public void setIdEstadistica(int idEstadistica) { this.idEstadistica = idEstadistica; }

    public int getIdJugador() { return idJugador; }
    public void setIdJugador(int idJugador) { this.idJugador = idJugador; }

    public String getNombreJugador() { return nombreJugador; }
    public void setNombreJugador(String nombreJugador) { this.nombreJugador = nombreJugador; }

    public int getIdPartido() { return idPartido; }
    public void setIdPartido(int idPartido) { this.idPartido = idPartido; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }

    public int getRebotes() { return rebotes; }
    public void setRebotes(int rebotes) { this.rebotes = rebotes; }

    public int getAsistencias() { return asistencias; }
    public void setAsistencias(int asistencias) { this.asistencias = asistencias; }

    public int getFaltasCometidas() { return faltasCometidas; }
    public void setFaltasCometidas(int faltasCometidas) { this.faltasCometidas = faltasCometidas; }
}