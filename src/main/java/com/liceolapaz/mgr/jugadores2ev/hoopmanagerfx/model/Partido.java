package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

import java.time.LocalDate;

public class Partido {
    private int idPartido;
    private LocalDate fecha;
    private String equipoRival;
    private String ubicacion;
    private int resultadoPropio;
    private int resultadoRival;

    public Partido() {
    }

    public Partido(int idPartido, LocalDate fecha, String equipoRival, String ubicacion, int resultadoPropio, int resultadoRival) {
        this.idPartido = idPartido;
        this.fecha = fecha;
        this.equipoRival = equipoRival;
        this.ubicacion = ubicacion;
        this.resultadoPropio = resultadoPropio;
        this.resultadoRival = resultadoRival;
    }

    public Partido(LocalDate fecha, String equipoRival, String ubicacion, int resultadoPropio, int resultadoRival) {
        this.fecha = fecha;
        this.equipoRival = equipoRival;
        this.ubicacion = ubicacion;
        this.resultadoPropio = resultadoPropio;
        this.resultadoRival = resultadoRival;
    }

    public int getIdPartido() { return idPartido; }
    public void setIdPartido(int idPartido) { this.idPartido = idPartido; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEquipoRival() { return equipoRival; }
    public void setEquipoRival(String equipoRival) { this.equipoRival = equipoRival; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public int getResultadoPropio() { return resultadoPropio; }
    public void setResultadoPropio(int resultadoPropio) { this.resultadoPropio = resultadoPropio; }

    public int getResultadoRival() { return resultadoRival; }
    public void setResultadoRival(int resultadoRival) { this.resultadoRival = resultadoRival; }
}