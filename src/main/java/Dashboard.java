import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private JTable tableMenu;
    private JButton addAMenuButton;
    private JButton modifyAMenuButton;
    private JButton deleteAMenuButton;
    private DefaultTableModel staffTableModel, saleTableModel, customerTableModel, menuTableModel;
    private String menuID, menuName, menuPrice;
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

        // Menu
        tableMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Select a row in Menu
        tableMenu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tableMenu.getSelectedRow();

                    // Make sure a row is selected
                    if (selectedRow != -1) {
                        // Get the data from the selected row
                        Object valueID = tableMenu.getValueAt(selectedRow, 0);
                        Object valueName = tableMenu.getValueAt(selectedRow, 1);
                        Object valuePrice = tableMenu.getValueAt(selectedRow, 2);

                        // Use the values as needed
                        menuID = valueID.toString();
                        menuName = valueName.toString();
                        menuPrice = valuePrice.toString();
                    }
                }
            }
        });
        // Add a menu
        addAMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMenuClass addMenuClass = new AddMenuClass(Dashboard.this);
                addMenuClass.pack();
                addMenuClass.setVisible(true);
            }
        });
        // Modify a menu
        modifyAMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuID != null) {
                    ModifyMenuClass modifyMenuClass = new ModifyMenuClass(Dashboard.this, menuName, menuPrice);
                    modifyMenuClass.pack();
                    modifyMenuClass.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an item.");
                }
            }
        });
        // Delete a menu
        deleteAMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuID != null) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "This item menu with id " + menuID + " name " + menuName + " will be deleted from the database. \n" +
                            "This action can't be undone!\n" +
                            "Are you sure want to delete?");
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        try (Connection connection = DatabaseHelper.getConnection()) {
                            assert connection != null;

                            String deleteQuery = "DELETE FROM menu WHERE id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                preparedStatement.setInt(1, Integer.parseInt(menuID));
                                preparedStatement.executeUpdate();
                            }

                            fetchMenuData();
                        } catch (SQLException ex) {
                            // Log the exception with relevant details
                            Logger logger = Logger.getLogger(Dashboard.class.getName()); // Assuming a Logger instance is available
                            logger.log(Level.SEVERE, "Error deleting menu data", ex);
                            // Optionally, display a user-friendly error message
                            JOptionPane.showMessageDialog(null, "An error occurred while deleting menu data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an item.");
                }
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
