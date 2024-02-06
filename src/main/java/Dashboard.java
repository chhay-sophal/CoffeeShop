import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.SQLException;
import java.util.Vector;

public class Dashboard extends JFrame implements UserCreatedListener {
    private static final String APPLICATIONS_FILE = "config.properties";
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane1;
    private JButton createAnEmployeeAccountButton;
    private JTable tableStaff;
    private JTable tableUser;
    private JButton createAUserAccountButton;
    private JTable tableSale;
    private JButton button1;
    private DefaultTableModel staffTableModel;

    public Dashboard() {
        fetchSaleData();
        fetchEmployeesData();

        createAnEmployeeAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the dialog when the button is clicked
                AddStaffClass createStaffDialog = new AddStaffClass(Dashboard.this);
                createStaffDialog.setSize(500, 300);
                createStaffDialog.pack();
                createStaffDialog.setVisible(true);
            }
        });

        createAUserAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void fetchSaleData() {
        String[] columnNames = {"Sale ID", "Order ID", "Item Name", "Amount", "Unit Price", "Total Price"};

        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT s.id AS sale_id, " +
                             "o.id AS order_id, " +
                             "m.name AS item_name, " +
                             "o.amount AS amount, " +
                             "m.price AS unit_price, " +
                             "o.total_price AS total_price " +
                             "FROM sold_items s " +
                             "JOIN order_items o ON s.order_item_id = o.id " +
                             "JOIN menu m ON o.item_id = m.id")) {

            DefaultTableModel saleTableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("sale_id"));
                row.add(resultSet.getInt("order_id"));
                row.add(resultSet.getString("item_name"));
                row.add(resultSet.getInt("amount"));
                row.add(resultSet.getDouble("unit_price"));
                row.add(resultSet.getDouble("total_price"));
                // Add more columns as needed

                saleTableModel.addRow(row);
            }

            tableSale.setModel(saleTableModel);

        } catch (SQLException e) {
            // Handle or log the exception appropriately
            e.printStackTrace();
        }
    }

    private void fetchEmployeesData() {
        String[] columnNames = {"ID", "Username", "Employee Type"};

        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT u.id, u.username, ut.type_name " +
                     "FROM users u " +
                     "INNER JOIN user_types ut ON u.user_type = ut.id " +
                     "WHERE u.user_type IN (1, 2)")) {

            // Create the table model with headers
            DefaultTableModel staffTableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("id"));
                row.add(resultSet.getString("username"));
                row.add(resultSet.getString("type_name"));

                staffTableModel.addRow(row);
            }

            // Set the table model to the JTable component
            tableStaff.setModel(staffTableModel);

        } catch (SQLException e) {
            // Handle or log the exception appropriately
            e.printStackTrace();
        }
    }

    private void fetchSaleData() {
        String[] columnNames = {"Sale ID", "Order ID", "Item Name", "Amount", "Unit Price", "Total Price"};

        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT s.id AS sale_id, " +
                             "o.id AS order_id, " +
                             "m.name AS item_name, " +
                             "o.amount AS amount, " +
                             "m.price AS unit_price, " +
                             "o.total_price AS total_price " +
                             "FROM sold_items s " +
                             "JOIN order_items o ON s.order_item_id = o.id " +
                             "JOIN menu m ON o.item_id = m.id")) {

            DefaultTableModel saleTableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("sale_id"));
                row.add(resultSet.getInt("order_id"));
                row.add(resultSet.getString("item_name"));
                row.add(resultSet.getInt("amount"));
                row.add(resultSet.getDouble("unit_price"));
                row.add(resultSet.getDouble("total_price"));
                // Add more columns as needed

                saleTableModel.addRow(row);
            }

            tableSale.setModel(saleTableModel);

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
        fetchEmployeesData(); // Fetch and add the updated data
    }

    public static void main(String[] args) throws SQLException {
        Dashboard dashboard = new Dashboard();
        dashboard.setContentPane(dashboard.dashboardPanel);
        dashboard.setTitle("Dashboard");
        dashboard.setSize(1000, 600);
        dashboard.setVisible(true);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
