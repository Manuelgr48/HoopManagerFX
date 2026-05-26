package com.liceolapaz.mgr.jugadores2ev.hoopmanagerfx.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection connection = null;

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
                    Properties prop = new Properties();

                    if (input == null) {
                        System.err.println("Error: No se ha encontrado el archivo config.properties");
                        return null;
                    }

                    prop.load(input);
                    String url = prop.getProperty("db.url");
                    String user = prop.getProperty("db.user");
                    String pass = prop.getProperty("db.password");

                    connection = DriverManager.getConnection(url, user, pass);
                    System.out.println("¡Conexión a la base de datos HoopManager establecida con éxito!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de SQL al conectar con la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al cargar la configuración de la BBDD: " + e.getMessage());
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}