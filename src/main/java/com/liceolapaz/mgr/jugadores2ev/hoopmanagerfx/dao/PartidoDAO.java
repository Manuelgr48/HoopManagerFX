package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Partido;
import java.util.List;

public interface PartidoDAO {
    void addPartido(Partido partido) throws Exception;
    void updatePartido(Partido partido) throws Exception;
    void deletePartido(int id) throws Exception;
    List<Partido> getAllPartidos() throws Exception;
    List<Partido> getPartidosPorEquipo(int idEquipo) throws Exception;
}