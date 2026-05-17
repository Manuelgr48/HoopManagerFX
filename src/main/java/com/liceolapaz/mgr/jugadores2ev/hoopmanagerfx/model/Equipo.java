package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

import java.time.LocalDate;

public class Equipo {
    private int idEquipo;
    private String nombre;
    private String categoria;
    private double presupuesto;
    private LocalDate fechaCreacion;

    public Equipo() {
    }

    public Equipo(int idEquipo, String nombre, String categoria, double presupuesto, LocalDate fechaCreacion) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
        this.fechaCreacion = fechaCreacion;
    }

    public Equipo(String nombre, String categoria, double presupuesto, LocalDate fechaCreacion) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdEquipo() { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getPresupuesto() { return presupuesto; }
    public void setPresupuesto(double presupuesto) { this.presupuesto = presupuesto; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return nombre + " (" + categoria + ")";
    }
}