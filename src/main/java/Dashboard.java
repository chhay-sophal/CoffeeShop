import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dashboard extends JFrame implements ItemCreatedListener {
    private static final String APPLICATIONS_FILE = "config.properties";
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane1;
    private JButton createAnEmployeeButton;
    private JTable tableStaff;
    private JTable tableCustomers;
    private JButton createACustomerButton;
    private JTable tableSale;
    private JButton deleteASaleItemButton;
    private JTable tableMenu;
    private JButton addAMenuButton;
    private JButton modifyAMenuButton;
    private JButton deleteAMenuButton;
    private DefaultTableModel staffTableModel, saleTableModel, customerTableModel, menuTableModel;

    public Dashboard() {
        fetchSaleData();
        fetchEmployeesData();
        fetchCustomersData();
        fetchMenuData();

        createAnEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the dialog when the button is clicked
                AddEmployeeClass addEmployeeClass = new AddEmployeeClass(Dashboard.this);
                addEmployeeClass.pack();
                addEmployeeClass.setVisible(true);
            }
        });

        createACustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the dialog when the button is clicked
                AddCustomerClass addCustomerClass = new AddCustomerClass(Dashboard.this);
                addCustomerClass.pack();
                addCustomerClass.setVisible(true);
            }
        });

        deleteASaleItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        addAMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMenuClass addMenuClass = new AddMenuClass(Dashboard.this);
                addMenuClass.pack();
                addMenuClass.setVisible(true);
            }
        });
    }

    private void fetchSaleData() {
        String[] columnNames = {"Sale ID", "Order ID", "Item Name", "Amount", "Unit Price", "Total Price"};

        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
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

                saleTableModel = new DefaultTableModel(columnNames, 0);

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

            }
        } catch (SQLException e) {
            // Log the exception with relevant details
            Logger logger = Logger.getLogger(Dashboard.class.getName()); // Assuming a Logger instance is available
            logger.log(Level.SEVERE, "Error fetching sale data", e);
            // Optionally, display a user-friendly error message
            JOptionPane.showMessageDialog(null, "An error occurred while fetching sale data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchEmployeesData() {
        String[] columnNames = {"ID", "Username", "Employee Type"};

        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT u.id, u.username, ut.type_name " +
                         "FROM users u " +
                         "INNER JOIN user_types ut ON u.user_type = ut.id " +
                         "WHERE u.user_type IN (1, 2)")) {

                // Create the table model with headers
                staffTableModel = new DefaultTableModel(columnNames, 0);

                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(resultSet.getInt("id"));
                    row.add(resultSet.getString("username"));
                    row.add(resultSet.getString("type_name"));

                    staffTableModel.addRow(row);
                }

                // Set the table model to the JTable component
                tableStaff.setModel(staffTableModel);

            }
        } catch (SQLException e) {
            // Log the exception with relevant details
            Logger logger = Logger.getLogger(Dashboard.class.getName()); // Assuming a Logger instance is available
            logger.log(Level.SEVERE, "Error fetching employee data", e);
            // Optionally, display a user-friendly error message
            JOptionPane.showMessageDialog(null, "An error occurred while fetching employee data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchCustomersData() {
        String[] columnNames = {"ID", "Username", "User Type"};

        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT u.id, u.username, ut.type_name " +
                         "FROM users u " +
                         "INNER JOIN user_types ut ON u.user_type = ut.id " +
                         "WHERE u.user_type = 3")) {

                // Create the table model with headers
                customerTableModel = new DefaultTableModel(columnNames, 0);

                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(resultSet.getInt("id"));
                    row.add(resultSet.getString("username"));
                    row.add(resultSet.getString("type_name"));

                    customerTableModel.addRow(row);
                }

                // Set the table model to the JTable component
                tableCustomers.setModel(customerTableModel);

            }
        } catch (SQLException e) {
            // Log the exception with relevant details
            Logger logger = Logger.getLogger(Dashboard.class.getName()); // Assuming a Logger instance is available
            logger.log(Level.SEVERE, "Error fetching customer data", e);
            // Optionally, display a user-friendly error message
            JOptionPane.showMessageDialog(null, "An error occurred while fetching customer data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchMenuData() {
        String[] columnNames = {"ID", "Name", "Price"};

        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT id, name, price FROM menu")) {

                // Create the table model with headers
                menuTableModel = new DefaultTableModel(columnNames, 0);

                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(resultSet.getInt("id"));
                    row.add(resultSet.getString("name"));
                    row.add(resultSet.getString("price"));

                    menuTableModel.addRow(row);
                }

                // Set the table model to the JTable component
                tableMenu.setModel(menuTableModel);

            }
        } catch (SQLException e) {
            // Log the exception with relevant details
            Logger logger = Logger.getLogger(Dashboard.class.getName()); // Assuming a Logger instance is available
            logger.log(Level.SEVERE, "Error fetching menu data", e);
            // Optionally, display a user-friendly error message
            JOptionPane.showMessageDialog(null, "An error occurred while fetching menu data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onItemCreated() {
        // Refresh the table data when a new user is created
        refreshTableData();
    }

    private void refreshTableData() {
        staffTableModel.setRowCount(0); // Clear existing data
        saleTableModel.setRowCount(0);
        customerTableModel.setRowCount(0);
        menuTableModel.setRowCount(0);

        fetchEmployeesData(); // Fetch and add the updated data
        fetchSaleData();
        fetchCustomersData();
        fetchMenuData();
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
