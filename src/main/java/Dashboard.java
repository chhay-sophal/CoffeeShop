import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.SQLException;
import java.util.Vector;

public class Dashboard extends JFrame implements UserCreatedListener {
    private static final String APPLICATIONS_FILE = "config.properties";
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane1;
    private JButton createAStaffAccountButton;
    private JTable tableStaff;
    private DefaultTableModel staffTableModel;

    public Dashboard() {

        createAStaffAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the dialog when the button is clicked
                AddStaffClass createStaffDialog = new AddStaffClass(Dashboard.this);
                createStaffDialog.setSize(500,300);
                createStaffDialog.pack();
                createStaffDialog.setVisible(true);
            }
        });

        // Initialize table model and set it to the table
//        staffTableModel = new DefaultTableModel();
        String[] columnNames = {"ID", "Username", "Employee Type"};
        staffTableModel = new DefaultTableModel(columnNames, 0);
        tableStaff.setModel(staffTableModel);

        // Fetch staff data from the database
        fetchStaffData();
    }

    private void fetchStaffData() {
        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT u.id, u.username, ut.type_name " +
                     "FROM users u " +
                     "INNER JOIN user_types ut ON u.user_type = ut.id " +
                     "WHERE u.user_type IN (1, 2)")) {

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("id"));
                row.add(resultSet.getString("username"));
                row.add(resultSet.getString("type_name"));
                // Add more columns as needed

                staffTableModel.addRow(row);
            }

        } catch (SQLException e) {
            // Handle or log the exception appropriately
            e.printStackTrace();
        }
    }

    @Override
    public void onUserCreated() {
        // Refresh the table data when a new user is created
        refreshTableData();
    }

    private void refreshTableData() {
        staffTableModel.setRowCount(0); // Clear existing data
        fetchStaffData(); // Fetch and add the updated data
    }

    private static int getRowCount(ResultSet resultSet) throws SQLException {
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        return rowCount;
    }

    public static void main(String[] args) throws SQLException {
        Dashboard dashboard = new Dashboard();
        dashboard.setContentPane(dashboard.dashboardPanel);
        dashboard.setTitle("Dashboard");
        dashboard.setSize(500,300);
        dashboard.setVisible(true);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
