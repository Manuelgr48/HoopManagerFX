package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;

import java.util.List;

public interface EstadisticaDAO {
    List<Estadistica> obtenerTodas();

    List<Estadistica> obtenerPorJugador(int idJugador);

    List<Estadistica> obtenerPorPartido(int idPartido);

    boolean insertar(Estadistica estadistica);

    boolean actualizar(Estadistica estadistica);

    boolean eliminar(int id);

    void addEstadistica(Estadistica estadistica) throws Exception;

    void updateEstadistica(Estadistica estadistica) throws Exception;

    void deleteEstadistica(int id) throws Exception;

    List<Estadistica> getAllEstadisticas() throws Exception;
}