package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.PartidoDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.PartidoDAOImpl;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;

import java.util.List;

public class PartidoService {

    private final PartidoDAO partidoDAO = new PartidoDAOImpl();

    public List<Partido> obtenerTodosLosPartidos() throws Exception {
        return partidoDAO.getAllPartidos();
    }

    public void registrarPartido(Partido partido) throws Exception {
        if (partido.getResultadoPropio() < 0 || partido.getResultadoRival() < 0) {
            throw new Exception("Los resultados no pueden ser negativos.");
        }
        partidoDAO.addPartido(partido);
    }

    public void actualizarPartido(Partido partido) throws Exception {
        if (partido.getResultadoPropio() < 0 || partido.getResultadoRival() < 0) {
            throw new Exception("Los resultados no pueden ser negativos.");
        }
        partidoDAO.updatePartido(partido);
    }

    public void eliminarPartido(int idPartido) throws Exception {
        partidoDAO.deletePartido(idPartido);
    }
}