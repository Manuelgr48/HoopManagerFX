package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.EstadisticaDAOImpl;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Estadistica;

import java.util.List;

public class EstadisticaService {

    private final EstadisticaDAO estadisticaDAO = new EstadisticaDAOImpl();

    public List<Estadistica> obtenerTodasLasEstadisticas() throws Exception {
        return estadisticaDAO.getAllEstadisticas();
    }

    public void registrarEstadistica(Estadistica estadistica) throws Exception {
        validarEstadistica(estadistica);
        estadisticaDAO.addEstadistica(estadistica);
    }

    public void actualizarEstadistica(Estadistica estadistica) throws Exception {
        validarEstadistica(estadistica);
        estadisticaDAO.updateEstadistica(estadistica);
    }

    public void eliminarEstadistica(int idEstadistica) throws Exception {
        estadisticaDAO.deleteEstadistica(idEstadistica);
    }

    private void validarEstadistica(Estadistica estadistica) throws Exception {
        if (estadistica.getPuntos() < 0 || estadistica.getRebotes() < 0 ||
                estadistica.getAsistencias() < 0 || estadistica.getFaltasCometidas() < 0) {
            throw new Exception("Los valores estadísticos no pueden ser negativos.");
        }
    }
}