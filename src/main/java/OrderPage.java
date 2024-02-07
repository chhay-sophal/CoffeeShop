import database.DatabaseHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderPage extends JFrame {
    private JPanel mainPanel;
    private JPanel itemPanel;
    private JPanel americanoPanel;
    private JPanel lattePanel;
    private JPanel espressoPanel;
    private JPanel capucinoPanel;
    private JButton buttonByAmericano;
    private JButton buttonByLatte;
    private JButton buttonByCapucino;
    private JButton buttonByEspresso;
    private JButton orderedItemButton;
    private JButton logOutButton;
    private int userID;

    public OrderPage(int userID) {
        this.userID = userID;
        // Initialize
        setContentPane(mainPanel);
        setTitle("HomePage");
        setSize(1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        buttonByAmericano.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseHelper.getConnection()) {
                    assert connection != null;

                    String insertSql = "INSERT INTO order_items (item_id, user_id, amount, total_price, paid, completed) VALUES (1, ?, 1, 3.5, 0, 0)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Execute the SQL statement
                        preparedStatement.setInt(1, userID);

                        preparedStatement.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(mainPanel, "Order created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException exception) {
                    // Log the exception with relevant details
                    Logger logger = Logger.getLogger(Dashboard.class.getName());
                    logger.log(Level.SEVERE, "Error fetching data", exception);
                    // Optionally, display a user-friendly error message
                    JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonByLatte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseHelper.getConnection()) {
                    assert connection != null;

                    String insertSql = "INSERT INTO order_items (item_id, user_id, amount, total_price, paid, completed) VALUES (1, ?, 1, 4, 0, 0)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Execute the SQL statement
                        preparedStatement.setInt(1, userID);

                        preparedStatement.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(mainPanel, "Order created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException exception) {
                    // Log the exception with relevant details
                    Logger logger = Logger.getLogger(Dashboard.class.getName());
                    logger.log(Level.SEVERE, "Error fetching data", exception);
                    // Optionally, display a user-friendly error message
                    JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonByCapucino.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseHelper.getConnection()) {
                    assert connection != null;

                    String insertSql = "INSERT INTO order_items (item_id, user_id, amount, total_price, paid, completed) VALUES (1, ?, 1, 4.5, 0, 0)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Execute the SQL statement
                        preparedStatement.setInt(1, userID);

                        preparedStatement.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(mainPanel, "Order created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException exception) {
                    // Log the exception with relevant details
                    Logger logger = Logger.getLogger(Dashboard.class.getName());
                    logger.log(Level.SEVERE, "Error fetching data", exception);
                    // Optionally, display a user-friendly error message
                    JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonByEspresso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseHelper.getConnection()) {
                    assert connection != null;

                    String insertSql = "INSERT INTO order_items (item_id, user_id, amount, total_price, paid, completed) VALUES (1, ?, 1, 3, 0, 0)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Execute the SQL statement
                        preparedStatement.setInt(1, userID);
                        preparedStatement.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(mainPanel, "Order created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException exception) {
                    // Log the exception with relevant details
                    Logger logger = Logger.getLogger(Dashboard.class.getName());
                    logger.log(Level.SEVERE, "Error fetching data", exception);
                    // Optionally, display a user-friendly error message
                    JOptionPane.showMessageDialog(null, "An error occurred while fetching data. Please check the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        orderedItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrderedItem(userID);
                dispose();
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogInPage();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new OrderPage(1);
    }
}
