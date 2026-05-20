package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;

public enum View {
    LOGIN("login-view.fxml"),
    DASHBOARD("dashboard-view.fxml"),
    EQUIPOS("equipos-view.fxml"),
    JUGADORES("jugadores-view.fxml");

    private final String fxmlFile;

    View(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}