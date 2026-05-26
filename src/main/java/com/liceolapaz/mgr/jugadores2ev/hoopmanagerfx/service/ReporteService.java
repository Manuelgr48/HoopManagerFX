package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.dao.JugadorDAO;
import com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model.Jugador;

import java.io.FileOutputStream;
import java.util.List;

public class ReporteService {

    private final JugadorDAO jugadorDAO = new JugadorDAO();

    public void generarInformeJugadores(String rutaDestino) throws Exception {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
        Paragraph titulo = new Paragraph("Informe Oficial - HoopManager", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);

        String[] cabeceras = {"Dorsal", "Nombre", "Apellidos", "Posición"};
        for (String cabecera : cabeceras) {
            PdfPCell celda = new PdfPCell(new Phrase(cabecera, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(8);
            tabla.addCell(celda);
        }

        List<Jugador> jugadores = jugadorDAO.obtenerTodos();

        for (Jugador j : jugadores) {
            tabla.addCell(String.valueOf(j.getDorsal()));
            tabla.addCell(j.getNombre());
            tabla.addCell(j.getApellidos());
            tabla.addCell(j.getPosicion());
        }

        documento.add(tabla);
        documento.close();
    }
}