import database.DatabaseHelper;

import javax.swing.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Dashboard extends JFrame {
    private static final String APPLICATIONS_FILE = "config.properties";
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane1;

    public Dashboard() {

    }

    private static int getRowCount(ResultSet resultSet) throws SQLException {
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        return rowCount;
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM employees")) {

                // Your result processing code here
                // Get employee data using column names (replace with actual column names)
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                // Print employee information in a formatted way
                System.out.println("Employee ID: " + id);
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
                System.out.println("--------------------");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Dashboard dashboard = new Dashboard();
        dashboard.setContentPane(dashboard.dashboardPanel);
        dashboard.setTitle("Dashboard");
        dashboard.setSize(500,300);
        dashboard.setVisible(true);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
