package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;

public class Jugador {
    private int idJugador;
    private String nombre;
    private String apellidos;
    private int dorsal;
    private String posicion;
    private double altura;
    private Integer idEquipo;
    private String nombreEquipo;

    public Jugador(int idJugador, String nombre, String apellidos, int dorsal, String posicion, double altura, Integer idEquipo, String nombreEquipo) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.altura = altura;
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
    }

    public int getIdJugador() { return idJugador; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public int getDorsal() { return dorsal; }
    public String getPosicion() { return posicion; }
    public double getAltura() { return altura; }
    public Integer getIdEquipo() { return idEquipo; }

    public String getNombreEquipo() {
        return (nombreEquipo != null && !nombreEquipo.isEmpty()) ? nombreEquipo : "Agente Libre";
    }
}