module com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires java.desktop;


    exports com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx;
    opens com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx to javafx.fxml;
    exports com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller;
    opens com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.controller to javafx.fxml;
    exports com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;
    opens com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util to javafx.fxml;
    exports com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.model;
}