import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderManagement extends JFrame {
    private JTable tableOrder;
    private JPanel orderPanel;
    private JButton logOutButton;
    private DefaultTableModel tableModel;

    public OrderManagement() {
        tableModel = new DefaultTableModel();
        fetchDataFromDatabase();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(orderPanel);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogInPage();
                dispose();
            }
        });
    }

    private void fetchDataFromDatabase() {
        String query = "SELECT * FROM order_items";
        Object[] columnNames = {"ID", "Item ID", "User ID", "Amount", "Total Price", "Paid", "Completed", "Action", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0);

        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                ResultSetMetaData metaData = resultSet.getMetaData();

                // Print the column names from the database
                int columnCount = metaData.getColumnCount();
                System.out.println("Columns from the database:");
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    System.out.println(columnName);
                }
                System.out.println();

                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= (columnNames.length - 2); i++) {
                        row.add(resultSet.getObject(i));
                    }
                    if ((Integer) resultSet.getObject(6) == 1) {
                        row.add("Set as paid");
                    } else {
                        row.add("Set as unpaid");
                    }
                    if ((Integer) resultSet.getObject(7) == 1) {
                        row.add("Make as complete");
                    } else {
                        row.add("Make as incomplete");
                    }
                    tableModel.addRow(row);
                }


                tableOrder.setModel(tableModel);
                tableOrder.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
                tableOrder.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JTextField()));
                tableOrder.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
                tableOrder.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JTextField()));

            }
        } catch (SQLException e) {
            // Log the exception with relevant details
            Logger logger = Logger.getLogger(Dashboard.class.getName());
            logger.log(Level.SEVERE, "Error fetching data", e);
            // Optionally, display a user-friendly error message
            JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ButtonRenderer extends DefaultTableCellRenderer {
        private JButton button;

        public ButtonRenderer() {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                // Handle button click action
                int row = tableOrder.convertRowIndexToModel(tableOrder.getEditingRow());
                int column = tableOrder.convertColumnIndexToModel(tableOrder.getEditingColumn());
                System.out.println("Button clicked in row " + row + ", column " + column);
            });
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            button.setText(value.toString());
            return button;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private Object clickedValue;

        public ButtonEditor(JTextField textField) {
            super(textField);
            setClickCountToStart(1);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                // Handle button click action
                int row = tableOrder.convertRowIndexToModel(tableOrder.getEditingRow());
                int column = tableOrder.convertColumnIndexToModel(tableOrder.getEditingColumn());
                Integer OrderID = (Integer) tableOrder.getValueAt(row, 0);
                Integer paid = (Integer) tableOrder.getValueAt(row, 5);
                Integer completed = (Integer) tableOrder.getValueAt(row, 6);
                System.out.println("Button clicked in row " + row + ", column " + column + ", orderID = " + OrderID);

                //Apply action to buttons in column
                if(column == 7){
                    int dialogResult = JOptionPane.showConfirmDialog(null, "This order with id " + OrderID + " will be marked as completed. \n" +
                            "Are you sure want to marked as completed?");

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        System.out.println("7");
                        String query = "UPDATE order_items SET completed = 1 WHERE id = ?";
                        if (completed == 1) {
                            query = "UPDATE order_items SET completed = 0 WHERE id = ?";
                        }
                        try (Connection connection = DatabaseHelper.getConnection()) {
                            assert connection != null;
                            try (PreparedStatement statement = connection.prepareStatement(query)) {
                                statement.setInt(1, OrderID);
                                int rowsAffected = statement.executeUpdate();
                                System.out.println("Rows affected: " + rowsAffected);
                                //Reload the table Data
                                fetchDataFromDatabase();

                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details." + ex, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else if(column == 8){
                    int dialogResult = JOptionPane.showConfirmDialog(null, "This order with id " + OrderID + " will be marked as paid. \n" +
                            "Are you sure want to marked as paid?");

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        System.out.println("8");
                        String query = "UPDATE order_items SET paid = 1 WHERE id = ? ";

                        if (paid == 1) {
                            query = "UPDATE order_items SET paid = 0 WHERE id = ?";
                        }

                        try (Connection connection = DatabaseHelper.getConnection()) {
                            assert connection != null;
                            try (PreparedStatement statement = connection.prepareStatement(query)) {
                                statement.setInt(1, OrderID);
                                int rowsAffected = statement.executeUpdate();
                                System.out.println("Rows affected: " + rowsAffected);
                                //Reload the table Data
                                fetchDataFromDatabase();
                            }

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            clickedValue = value;
            button.setText(value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return clickedValue; // Return the stored value
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderManagement());
    }

}
