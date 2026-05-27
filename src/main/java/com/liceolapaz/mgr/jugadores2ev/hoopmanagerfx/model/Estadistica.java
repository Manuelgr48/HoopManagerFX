package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

public class Estadistica {
    private int idEstadistica;
    private int idJugador;
    private String nombreJugador;
    private int idPartido;
    private String equipoRival;
    private int puntos;
    private int rebotes;
    private int asistencias;
    private int faltasCometidas;

    public Estadistica(int idEstadistica, int idJugador, String nombreJugador, int idPartido, int puntos, int rebotes, int asistencias, int faltasCometidas) {
        this(idEstadistica, idJugador, nombreJugador, idPartido, null, puntos, rebotes, asistencias, faltasCometidas);
    }

    public Estadistica(int idEstadistica, int idJugador, String nombreJugador, int idPartido, String equipoRival, int puntos, int rebotes, int asistencias, int faltasCometidas) {
        this.idEstadistica = idEstadistica;
        this.idJugador = idJugador;
        this.nombreJugador = nombreJugador;
        this.idPartido = idPartido;
        this.equipoRival = equipoRival;
        this.puntos = puntos;
        this.rebotes = rebotes;
        this.asistencias = asistencias;
        this.faltasCometidas = faltasCometidas;
    }

    public int getIdEstadistica() { return idEstadistica; }
    public int getIdJugador() { return idJugador; }
    public String getNombreJugador() { return nombreJugador; }
    public int getIdPartido() { return idPartido; }

    public String getEquipoRival() {
        return equipoRival != null && !equipoRival.isEmpty() ? equipoRival : "Sin partido";
    }

    public int getPuntos() { return puntos; }
    public int getRebotes() { return rebotes; }
    public int getAsistencias() { return asistencias; }
    public int getFaltasCometidas() { return faltasCometidas; }
}