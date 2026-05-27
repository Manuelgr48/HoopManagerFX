package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;
import net.sf.jasperreports.engine.*;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReporteService {

    public void generarInformeJugadores(String rutaDestino) throws Exception {
        InputStream reportStream = getClass().getResourceAsStream("/reportes/jugadores.jrxml");

        if (reportStream == null) {
            throw new Exception("No se encontro el archivo de diseno del reporte jugadores.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        Connection conn = DatabaseConnection.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);
        JasperExportManager.exportReportToPdfFile(jasperPrint, rutaDestino);
    }

    public void generarInformeEquipo(String rutaDestino, int idEquipo, String nombreEquipo) throws Exception {
        InputStream reportStream = getClass().getResourceAsStream("/reportes/equipo-jugadores.jrxml");

        if (reportStream == null) {
            throw new Exception("No se encontro el archivo de diseno del reporte equipo-jugadores.jrxml");
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("ID_EQUIPO", idEquipo);
        parametros.put("NOMBRE_EQUIPO", nombreEquipo);

        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        Connection conn = DatabaseConnection.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn);
        JasperExportManager.exportReportToPdfFile(jasperPrint, rutaDestino);
    }
}