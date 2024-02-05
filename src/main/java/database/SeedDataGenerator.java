package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SeedDataGenerator {

    public static void main(String[] args) {
        try (Connection connection = DatabaseHelper.getConnection()) {
            if (connection != null) {
                try (Statement statement = connection.createStatement()) {
                    executeScript(statement, "src/main/resources/db/seed-data.sql");
                    System.out.println("Seed data inserted successfully!");
                }
            } else {
                System.err.println("Failed to obtain a database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeScript(Statement statement, String scriptFilePath) throws SQLException {
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFilePath))) {
            String line;
            StringBuilder script = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
            statement.executeUpdate(script.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
