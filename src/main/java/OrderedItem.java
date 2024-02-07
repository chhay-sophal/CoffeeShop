import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderedItem extends JFrame {
    private JPanel mainPanel;
    private JTable tableOrder;
    private JButton goBackButton;
    private DefaultTableModel tableModel;

    private int userId;

    public OrderedItem(int userId) {
        this.userId = userId;
        setContentPane(mainPanel);
        setTitle("HomePage");
        setSize(1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String query = "SELECT id, item_id, amount, total_price FROM order_items WHERE completed = 0 AND user_id = ?";
        Object[] columnNames = {"ID", "Item ID", "Amount", "Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0);

        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Set the user_id parameter value (assuming it's an int, replace it with the actual type)
                preparedStatement.setInt(1, this.userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
                        for (int i = 1; i <= columnCount; i++) {
                            row.add(resultSet.getObject(i));
                        }
                        tableModel.addRow(row);
                    }

                    tableOrder.setModel(tableModel);
                }
            }
        } catch (SQLException e) {
            // Log the exception with relevant details
            Logger logger = Logger.getLogger(Dashboard.class.getName());
            logger.log(Level.SEVERE, "Error fetching data", e);
            // Optionally, display a user-friendly error message
            JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderPage(userId);
                dispose();
            }
        });
    }
}
