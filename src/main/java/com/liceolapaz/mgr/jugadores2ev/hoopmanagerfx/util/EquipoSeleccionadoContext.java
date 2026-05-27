package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Equipo;

public class EquipoSeleccionadoContext {
    private static Equipo equipoSeleccionado;

    public static void setEquipoSeleccionado(Equipo equipo) {
        equipoSeleccionado = equipo;
    }

    public static Equipo getEquipoSeleccionado() {
        return equipoSeleccionado;
    }

    public static void limpiar() {
        equipoSeleccionado = null;
    }
}