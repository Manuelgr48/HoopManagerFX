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

    public List<Partido> obtenerPartidosPorEquipo(int idEquipo) throws Exception {
        return partidoDAO.getPartidosPorEquipo(idEquipo);
    }

    public void registrarPartido(Partido partido) throws Exception {
        validarPartido(partido);
        partidoDAO.addPartido(partido);
    }

    public void actualizarPartido(Partido partido) throws Exception {
        validarPartido(partido);
        partidoDAO.updatePartido(partido);
    }

    public void eliminarPartido(int idPartido) throws Exception {
        partidoDAO.deletePartido(idPartido);
    }

    private void validarPartido(Partido partido) throws Exception {
        if (partido.getIdEquipo() == null) {
            throw new Exception("Debes asignar un equipo al partido.");
        }

        if (partido.getFecha() == null) {
            throw new Exception("La fecha es obligatoria.");
        }

        if (partido.getEquipoRival() == null || partido.getEquipoRival().trim().isEmpty()) {
            throw new Exception("El equipo rival es obligatorio.");
        }

        if (partido.getUbicacion() == null || partido.getUbicacion().trim().isEmpty()) {
            throw new Exception("La ubicacion es obligatoria.");
        }

        if (partido.getResultadoPropio() < 0 || partido.getResultadoRival() < 0) {
            throw new Exception("Los resultados no pueden ser negativos.");
        }
    }
}