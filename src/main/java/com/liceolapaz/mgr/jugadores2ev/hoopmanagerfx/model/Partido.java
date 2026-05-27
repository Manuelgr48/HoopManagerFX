package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

import java.time.LocalDate;

public class Partido {
    private int idPartido;
    private Integer idEquipo;
    private String nombreEquipo;
    private LocalDate fecha;
    private String equipoRival;
    private String ubicacion;
    private int resultadoPropio;
    private int resultadoRival;

    public Partido() {
    }

    public Partido(int idPartido, Integer idEquipo, String nombreEquipo, LocalDate fecha, String equipoRival, String ubicacion, int resultadoPropio, int resultadoRival) {
        this.idPartido = idPartido;
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.fecha = fecha;
        this.equipoRival = equipoRival;
        this.ubicacion = ubicacion;
        this.resultadoPropio = resultadoPropio;
        this.resultadoRival = resultadoRival;
    }

    public Partido(Integer idEquipo, LocalDate fecha, String equipoRival, String ubicacion, int resultadoPropio, int resultadoRival) {
        this.idEquipo = idEquipo;
        this.fecha = fecha;
        this.equipoRival = equipoRival;
        this.ubicacion = ubicacion;
        this.resultadoPropio = resultadoPropio;
        this.resultadoRival = resultadoRival;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo != null ? nombreEquipo : "Sin equipo";
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEquipoRival() {
        return equipoRival;
    }

    public void setEquipoRival(String equipoRival) {
        this.equipoRival = equipoRival;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getResultadoPropio() {
        return resultadoPropio;
    }

    public void setResultadoPropio(int resultadoPropio) {
        this.resultadoPropio = resultadoPropio;
    }

    public int getResultadoRival() {
        return resultadoRival;
    }

    public void setResultadoRival(int resultadoRival) {
        this.resultadoRival = resultadoRival;
    }
}