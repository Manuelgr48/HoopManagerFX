package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;

public enum View {
    INICIO("inicio-view.fxml", "Inicio", false),
    EQUIPOS("equipos-view.fxml", "Equipos", false),
    JUGADORES("jugadores-view.fxml", "Jugadores", false),
    PARTIDOS("partidos-view.fxml", "Partidos", false),
    USUARIOS("usuarios-view.fxml", "Gestion usuarios", true);

    private final String fxmlFile;
    private final String titulo;
    private final boolean soloAdmin;

    View(String fxmlFile, String titulo, boolean soloAdmin) {
        this.fxmlFile = fxmlFile;
        this.titulo = titulo;
        this.soloAdmin = soloAdmin;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isSoloAdmin() {
        return soloAdmin;
    }
}