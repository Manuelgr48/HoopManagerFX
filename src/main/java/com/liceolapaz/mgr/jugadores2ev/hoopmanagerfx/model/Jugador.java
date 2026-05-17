package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

public class Jugador {
    private int idJugador;
    private String nombre;
    private String apellidos;
    private int dorsal;
    private String posicion;
    private double altura;
    private int idEquipo;

    public Jugador() {
    }

    public Jugador(int idJugador, String nombre, String apellidos, int dorsal, String posicion, double altura, int idEquipo) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.altura = altura;
        this.idEquipo = idEquipo;
    }

    public Jugador(String nombre, String apellidos, int dorsal, String posicion, double altura, int idEquipo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.altura = altura;
        this.idEquipo = idEquipo;
    }

    public int getIdJugador() { return idJugador; }
    public void setIdJugador(int idJugador) { this.idJugador = idJugador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public int getDorsal() { return dorsal; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public int getIdEquipo() { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " (#" + dorsal + ")";
    }
}