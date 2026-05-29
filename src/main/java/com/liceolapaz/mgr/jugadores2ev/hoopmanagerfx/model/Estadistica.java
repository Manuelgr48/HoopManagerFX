package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

import java.time.LocalDate;

public class Estadistica {
    private int idEstadistica;
    private int idJugador;
    private String nombreJugador;
    private Integer idEquipoJugador;
    private String nombreEquipoJugador;
    private int idPartido;
    private String equipoRival;
    private LocalDate fechaPartido;
    private int puntos;
    private int rebotes;
    private int asistencias;
    private int faltasCometidas;

    public Estadistica(int idEstadistica, int idJugador, String nombreJugador, int idPartido, int puntos, int rebotes, int asistencias, int faltasCometidas) {
        this(idEstadistica, idJugador, nombreJugador, null, null, idPartido, null, null, puntos, rebotes, asistencias, faltasCometidas);
    }

    public Estadistica(int idEstadistica, int idJugador, String nombreJugador, int idPartido, String equipoRival, int puntos, int rebotes, int asistencias, int faltasCometidas) {
        this(idEstadistica, idJugador, nombreJugador, null, null, idPartido, equipoRival, null, puntos, rebotes, asistencias, faltasCometidas);
    }

    public Estadistica(int idEstadistica, int idJugador, String nombreJugador, Integer idEquipoJugador,
                       String nombreEquipoJugador, int idPartido, String equipoRival,
                       LocalDate fechaPartido, int puntos, int rebotes,
                       int asistencias, int faltasCometidas) {
        this.idEstadistica = idEstadistica;
        this.idJugador = idJugador;
        this.nombreJugador = nombreJugador;
        this.idEquipoJugador = idEquipoJugador;
        this.nombreEquipoJugador = nombreEquipoJugador;
        this.idPartido = idPartido;
        this.equipoRival = equipoRival;
        this.fechaPartido = fechaPartido;
        this.puntos = puntos;
        this.rebotes = rebotes;
        this.asistencias = asistencias;
        this.faltasCometidas = faltasCometidas;
    }

    public int getIdEstadistica() { return idEstadistica; }
    public int getIdJugador() { return idJugador; }
    public String getNombreJugador() { return nombreJugador != null ? nombreJugador : "Jugador desconocido"; }
    public Integer getIdEquipoJugador() { return idEquipoJugador; }
    public String getNombreEquipoJugador() { return nombreEquipoJugador != null ? nombreEquipoJugador : "Sin equipo"; }
    public int getIdPartido() { return idPartido; }
    public String getEquipoRival() { return equipoRival != null && !equipoRival.isEmpty() ? equipoRival : "Sin partido"; }
    public LocalDate getFechaPartido() { return fechaPartido; }
    public int getPuntos() { return puntos; }
    public int getRebotes() { return rebotes; }
    public int getAsistencias() { return asistencias; }
    public int getFaltasCometidas() { return faltasCometidas; }
}