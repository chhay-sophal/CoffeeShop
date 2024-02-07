import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    public JPanel dashboardPanel;
    private JTabbedPane tabbedPane1;
    private JButton createEmployeeButton;
    private JTable tableEmployee;
    private JTable tableCustomer;
    private JButton createCustomerButton;
    private JTable tableSale;
    private JTable tableMenu;
    private JButton createMenuButton;
    private JButton updateMenuButton;
    private JButton deleteMenuButton;
    private JButton deleteEmployeeButton;
    private JButton updateEmployeeButton;
    private JButton deleteCustomerButton;
    private JButton updateCustomerButton;
    private DefaultTableModel employeeTableModel, saleTableModel, customerTableModel, menuTableModel;
    private String userID, userUsername, password, userUserType;
    private String menuID, menuName, menuPrice;
    private static final String EMPLOYEE_QUERY = "SELECT u.id, u.username, u.password, ut.type_name " +
            "FROM users u " +
            "INNER JOIN user_types ut ON u.user_type = ut.id " +
            "WHERE u.user_type IN (1, 2)";

    private static final String CUSTOMER_QUERY = "SELECT u.id, u.username, ut.type_name " +
            "FROM users u " +
            "INNER JOIN user_types ut ON u.user_type = ut.id " +
            "WHERE u.user_type = 3";

    private static final String SALE_QUERY = "SELECT s.id AS sale_id, " +
            "o.id AS order_id, " +
            "m.name AS item_name, " +
            "o.amount AS amount, " +
            "m.price AS unit_price, " +
            "o.total_price AS total_price " +
            "FROM sold_items s " +
            "JOIN order_items o ON s.order_item_id = o.id " +
            "JOIN menu m ON o.item_id = m.id";

    private static final String MENU_QUERY = "SELECT id, name, price FROM menu";

    public Dashboard() {
        setContentPane(dashboardPanel);
        setTitle("Dashboard");
        setSize(1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fetchSaleData();
        fetchEmployeesData();
        fetchCustomersData();
        fetchMenuData();

        // Employee
        tableEmployee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Select a row in employee
        tableEmployee.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tableEmployee.getSelectedRow();

                    // Make sure a row is selected
                    if (selectedRow != -1) {
                        // Get the data from the selected row
                        Object valueID = tableEmployee.getValueAt(selectedRow, 0);
                        Object valueUsername = tableEmployee.getValueAt(selectedRow, 1);
                        Object valuePassword = tableEmployee.getValueAt(selectedRow, 2);
                        Object valueUserType = tableEmployee.getValueAt(selectedRow, 3);

                        // Use the values as needed
                        userID = valueID.toString();
                        userUsername = valueUsername.toString();
                        password = valuePassword.toString();
                        userUserType = valueUserType.toString();
                    }
                }
            }
        });
        // Create an employee
        createEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the dialog when the button is clicked
                AddEmployeeClass addEmployeeClass = new AddEmployeeClass(Dashboard.this);
                addEmployeeClass.pack();
                addEmployeeClass.setVisible(true);
            }
        });
        // Update an employee
        updateEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userID != null) {
                    UpdateEmployeeClass updateEmployeeClass = new UpdateEmployeeClass(Dashboard.this, Integer.parseInt(userID), userUsername, password, userUserType);
                    updateEmployeeClass.pack();
                    updateEmployeeClass.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an item.");
                }
            }
        });
        // Delete an employee
        deleteEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userID != null) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "This user with id " + userID + " username " + userUsername + " will be deleted from the database. \n" +
                            "This action can't be undone!\n" +
                            "Are you sure want to delete?");
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        try (Connection connection = DatabaseHelper.getConnection()) {
                            assert connection != null;

                            String deleteQuery = "DELETE FROM users WHERE id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                preparedStatement.setInt(1, Integer.parseInt(userID));
                                preparedStatement.executeUpdate();
                            }

                            fetchEmployeesData();
                        } catch (SQLException ex) {
                            // Log the exception with relevant details
                            Logger logger = Logger.getLogger(Dashboard.class.getName()); // Assuming a Logger instance is available
                            logger.log(Level.SEVERE, "Error deleting user data", ex);
                            // Optionally, display a user-friendly error message
                            JOptionPane.showMessageDialog(null, "An error occurred while deleting user data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an item.");
                }
            }
        });


        // Customer
        tableCustomer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Select a row in customer
        tableCustomer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tableCustomer.getSelectedRow();

                    // Make sure a row is selected
                    if (selectedRow != -1) {
                        // Get the data from the selected row
                        Object valueID = tableCustomer.getValueAt(selectedRow, 0);
                        Object valueUsername = tableCustomer.getValueAt(selectedRow, 1);
                        Object valueUserType = tableCustomer.getValueAt(selectedRow, 2);

                        userID = valueID.toString();
                        userUsername = valueUsername.toString();
                        userUserType = valueUserType.toString();

                        try (Connection connection = DatabaseHelper.getConnection()) {
                            assert connection != null;

                            String deleteQuery = "SELECT password FROM users WHERE id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                preparedStatement.setInt(1, Integer.parseInt(userID));

                                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                    // Check if there is a result (row) in the ResultSet
                                    if (resultSet.next()) {
                                        // Retrieve the password from the ResultSet
                                        password = resultSet.getString("password");
                                    } else {
                                        // Handle the case where no user with the given ID is found
                                        JOptionPane.showMessageDialog(null, "User with ID " + userID + " not found", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
        // Create a customer
        createCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the dialog when the button is clicked
                AddCustomerClass addCustomerClass = new AddCustomerClass(Dashboard.this);
                addCustomerClass.pack();
                addCustomerClass.setVisible(true);
            }
        });
        // Update a customer
        updateCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userID != null) {
                    UpdateCustomerClass updateCustomerClass = new UpdateCustomerClass(Dashboard.this, Integer.parseInt(userID), password);
                    updateCustomerClass.pack();
                    updateCustomerClass.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please select an item.");
                }
            }
        });
        // Delete a customer
        deleteCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userID != null) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "This user with id " + userID + " username " + userUsername + " will be deleted from the database. \n" +
                            "This action can't be undone!\n" +
                            "Are you sure want to delete?");
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        try (Connection connection = DatabaseHelper.getConnection()) {
                            assert connection != null;

                            String deleteQuery = "DELETE FROM users WHERE id = ?";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                                preparedStatement.setInt(1, Integer.parseInt(userID));
                                preparedStatement.executeUpdate();
                            }

                            fetchCustomersData();
                        } catch (SQLException ex) {
                            // Log the exception with relevant details
                            Logger logger = Logger.getLogger(Dashboard.class.getName()); // Assuming a Logger instance is available
                            logger.log(Level.SEVERE, "Error deleting user data", ex);
                            // Optionally, display a user-friendly error message
                            JOptionPane.showMessageDialog(null, "An error occurred while deleting user data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an item.");
                }
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
        // Create a menu
        createMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMenuClass addMenuClass = new AddMenuClass(Dashboard.this);
                addMenuClass.pack();
                addMenuClass.setVisible(true);
            }
        });
        // Update a menu
        updateMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuID != null) {
                    UpdateMenuClass updateMenuClass = new UpdateMenuClass(Dashboard.this, Integer.parseInt(menuID), menuName, menuPrice);
                    updateMenuClass.pack();
                    updateMenuClass.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an item.");
                }
            }
        });
        // Delete a menu
        deleteMenuButton.addActionListener(new ActionListener() {
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

    private void fetchDataAndPopulateTable(String[] columnNames, DefaultTableModel tableModel, String query, int expectedColumns) {
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= expectedColumns; i++) {
                        row.add(resultSet.getObject(i));
                    }

                    tableModel.addRow(row);
                }

                // Set the table model to the JTable component
                if (tableModel == employeeTableModel) {
                    tableEmployee.setModel(tableModel);
                } else if (tableModel == customerTableModel) {
                    tableCustomer.setModel(customerTableModel);
                } else if (tableModel == saleTableModel) {
                    tableSale.setModel(saleTableModel);
                } else if (tableModel == menuTableModel) {
                    tableMenu.setModel(menuTableModel);
                }

            }
        } catch (SQLException e) {
            // Log the exception with relevant details
            Logger logger = Logger.getLogger(Dashboard.class.getName());
            logger.log(Level.SEVERE, "Error fetching data", e);
            // Optionally, display a user-friendly error message
            JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchEmployeesData() {
        String[] columnNames = {"ID", "Username", "Password", "Employee Type"};
        employeeTableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        fetchDataAndPopulateTable(columnNames, employeeTableModel, EMPLOYEE_QUERY, columnNames.length);
    }

    private void fetchCustomersData() {
        String[] columnNames = {"ID", "Username", "User Type"};
        customerTableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        fetchDataAndPopulateTable(columnNames, customerTableModel, CUSTOMER_QUERY, columnNames.length);
    }

    private void fetchSaleData() {
        String[] columnNames = {"Sale ID", "Order ID", "Item Name", "Amount", "Unit Price", "Total Price"};
        saleTableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        fetchDataAndPopulateTable(columnNames, saleTableModel, SALE_QUERY, columnNames.length);
    }

    private void fetchMenuData() {
        String[] columnNames = {"ID", "Name", "Price"};
        menuTableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        fetchDataAndPopulateTable(columnNames, menuTableModel, MENU_QUERY, columnNames.length);
    }
    public void onItemCreated() {
        // Refresh the table data when a new user is created
        refreshTableData();
    }

    private void refreshTableData() {
        employeeTableModel.setRowCount(0); // Clear existing data
        saleTableModel.setRowCount(0);
        customerTableModel.setRowCount(0);
        menuTableModel.setRowCount(0);

        fetchEmployeesData(); // Fetch and add the updated data
        fetchSaleData();
        fetchCustomersData();
        fetchMenuData();
    }

    public static void main(String[] args) throws SQLException {
        new Dashboard();
    }
}
