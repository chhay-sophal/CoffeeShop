package database;

import java.sql.*;

public class CreateDatabase {

    public static void main(String[] args) {
        String databaseName = "database.db"; // Replace with your desired name
        String databaseURL = "jdbc:sqlite:" + databaseName;

        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            if (conn != null) {
                System.out.println("Database created successfully!");
            } else {
                System.out.println("Failed to create database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
