module com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx to javafx.fxml;
    exports com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx;
}