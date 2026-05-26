package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service;

import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util.DatabaseConnection;
import net.sf.jasperreports.engine.*;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

public class ReporteService {

    public void generarInformeJugadores(String rutaDestino) throws Exception {
        InputStream reportStream = getClass().getResourceAsStream("/reportes/jugadores.jrxml");
        if (reportStream == null) {
            throw new Exception("No se encontró el archivo de diseño del reporte (jugadores.jrxml)");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        Connection conn = DatabaseConnection.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);
        JasperExportManager.exportReportToPdfFile(jasperPrint, rutaDestino);
    }
}