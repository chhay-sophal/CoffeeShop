
import Product.Product;
import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.util.List;
import java.sql.PreparedStatement;

public class HomePage extends JFrame {
    private JPanel panel1;
    private JLabel NameLabel;
    private JLabel PriceLabel;
    private JLabel PictureLabel;
    private JPanel OrderPanel;
    private JButton orderButton;
    private JButton deleteButton1;
    private JButton cancelButton;
    private JTable OrderTable;
    private JLabel image;
    private JTable table2;
    private JLabel totalPriceLabel;

    private static final String MENU_QUERY = "SELECT name, price FROM menu";

    private static final String ADD_ORDER = "INSERT INTO order_items (items_id, amount, total_price, paid, completed) VALUES (?, 1, ?, 1, 0)";

    // Create a table model
    DefaultTableModel model = new DefaultTableModel();
    ArrayList<Product> productList = new ArrayList<>();

    public HomePage() {
        ImageIcon Image = new ImageIcon("icon.png");
        image.setIcon(Image);

        String[] columnNames = {"Name", "Price"};

        model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        fetchDataAndPopulateTable(columnNames, model, MENU_QUERY, columnNames.length);
        table2.setModel(model);
        table2.setRowHeight(30);
        table2.setShowGrid(false);
        table2.setIntercellSpacing(new Dimension(0, 0));
        table2.setRowSelectionAllowed(false);
        table2.setColumnSelectionAllowed(false);

        DefaultTableModel modelorder = new DefaultTableModel(new Object[]{"Name", "Price"}, 0);

        table2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if the selection is not adjusting
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table2.getSelectedRow();
                    int selectedColumn = table2.getSelectedColumn();

                    Object nameObj = table2.getValueAt(selectedRow, 0); // Extract name from column 0
                    Object priceObj = table2.getValueAt(selectedRow, 1);

                    String name = nameObj.toString(); // Convert to String
                    double price = Double.parseDouble(priceObj.toString()); // Convert to double
                    Product product = new Product(name,price);

                    if (selectedRow != -1 && selectedColumn != -1) {

                        Object value = table2.getValueAt(selectedRow, selectedColumn);

                        int option = JOptionPane.showConfirmDialog(table2, "Do you want to buy " + value + "?"+nameObj, "Buy Confirmation", JOptionPane.YES_NO_OPTION);

                        if (option == JOptionPane.YES_OPTION) {

                            JOptionPane.showMessageDialog(table2, "You bought " + value + ".", "Purchase Success", JOptionPane.INFORMATION_MESSAGE);
                            productList.add(product);

//                            for (Product product1 : productList) {
//                                modelorder.addRow(new Object[]{product1.getName(), product1.getPrice()});
//                            }
                            modelorder.addRow(new Object[]{product.getName(), product.getPrice()});
                            OrderTable.setModel(modelorder);

                        } else {
                            // Cancel action
                            JOptionPane.showMessageDialog(table2, "You cancelled the purchase.", "Purchase Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        }

                        updateTotalPriceLabel(totalPriceLabel, OrderTable, 1); // A
                    }
                }

            }
        });
        deleteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = OrderTable.getSelectedRow();
                if (selectedRow != -1) {
                    productList.clear(); // Assuming ProductList is a list
                    modelorder.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(OrderTable, "Item deleted successfully.", "Delete Success", JOptionPane.INFORMATION_MESSAGE);

                } else {
                }
            }
        });
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(OrderTable, "Order placed successfully!", "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
                // Clear the table or perform any other necessary actions after placing the order
                modelorder.setRowCount(0); // Clear all rows from the order table

//                InsertOrder(productList, ADD_ORDER);

                totalPriceLabel.setText("Total Price: 0 " );

            }

        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action to be performed when the cancel button is clicked
                // This could involve clearing the order table, resetting any form fields, etc.
                // For example:
                modelorder.setRowCount(0); // Clear all rows from the order table
                JOptionPane.showMessageDialog(OrderTable, "Order cancelled.", "Cancellation", JOptionPane.INFORMATION_MESSAGE);
            }
        });


    }
    private void fetchMenuData() {
        String[] columnNames = {"Name", "Price"};
        model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        fetchDataAndPopulateTable(columnNames, model, MENU_QUERY, columnNames.length);
    }

    // Method to calculate and update total price label
    private static void updateTotalPriceLabel(JLabel label, JTable table, int columnIndex) {
        double total = 0.0;
        TableModel model = table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            // Convert the price String to Double before adding to total
            try {
                double price = Double.parseDouble(model.getValueAt(i, columnIndex).toString());
                total += price;
            } catch (NumberFormatException e) {
                // Handle invalid or empty price values
                System.err.println("Invalid price value at row " + i);
            }
        }
        label.setText("Total Price: " + total);
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
                if (tableModel == model) {
                    table2.setModel(model);
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
    private void refreshTableData() {

        model.setRowCount(0);

        fetchMenuData();
    }

    // productList should be passed as a parameter
    private void InsertOrder(List<Product> productList, String query) {
        try (Connection connection = DatabaseHelper.getConnection())  {
            // Create a PreparedStatement
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Batch insert objects
                for (Product product : productList) {
                    preparedStatement.setString(1, product.getName());
//                    preparedStatement.setDouble(2, product.getPrice());
                    // You may need to set other parameters according to your database schema
                    preparedStatement.addBatch();
                }

                // Execute batch insert
                int[] rowsAffected = preparedStatement.executeBatch();
                System.out.println(rowsAffected.length + " row(s) affected.");
            }
        } catch (SQLException e) {
            // Log the exception with relevant details
            Logger logger = Logger.getLogger(Dashboard.class.getName());
            logger.log(Level.SEVERE, "Error inserting data", e);
            // Optionally, display a user-friendly error message
            JOptionPane.showMessageDialog(null, "An error occurred while inserting data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            HomePage homePage = new HomePage();
            homePage.setContentPane(homePage.panel1);
//            homePage.getContentPane().add(label);
            homePage.setTitle("HomePage");
            homePage.setSize(1000, 600);
            homePage.setVisible(true);
            homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
