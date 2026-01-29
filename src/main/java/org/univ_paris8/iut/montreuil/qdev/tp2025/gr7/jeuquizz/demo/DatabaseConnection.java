package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/MasterAnnonce";
    private static final String USER = "test";
    private static final String PASSWORD = "test";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
