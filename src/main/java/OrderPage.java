import database.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
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

    public OrderPage() {
        // Initialize
        setContentPane(mainPanel);
        setTitle("HomePage");
        setSize(1000, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        ImageIcon americanoIcon = new ImageIcon()
//        americanoImg.setSize(100,100);
        // Set image
//        ImageIcon IconAmericano = new ImageIcon("src/main/java/images/americano.jpg");
//        ImageIcon IconLatte = new ImageIcon("src/main/java/images/latte.jng");
//        ImageIcon IconCapucino = new ImageIcon("src/main/java/images/capucino.jng");
//        ImageIcon IconEspresso = new ImageIcon("src/main/java/images/espresso.jng");
//        americanoImg.setIcon(IconAmericano);
//        latteImg.setIcon(IconLatte);
//        capucinoImg.setIcon(IconCapucino);
//        espressoImg.setIcon(IconEspresso);
//        americanoImg.setSize(100, 100);
//        latteImg.setSize(100, 100);
//        capucinoImg.setSize(100, 100);
//        espressoImg.setSize(100, 100);

//        ImageIcon originalIcon = new ImageIcon("src/main/java/images/americano.jpg");
//        Image originalImage = originalIcon.getImage();
//        int originalWidth = originalImage.getWidth(null);
//        int originalHeight = originalImage.getHeight(null);
//        int newWidth = 100;
//        int newHeight = (int) Math.round((double) newWidth / originalWidth * originalHeight);
//        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
//        ImageIcon resizedIcon = new ImageIcon(resizedImage);
//        americanoImg.setIcon(resizedIcon);
//        americanoImg.setText("");
//
//        ImageIcon originalIcon1 = new ImageIcon("src/main/java/images/latteImg.jpg");
//        Image originalImage1 = originalIcon1.getImage();
//        int originalWidth1 = originalImage1.getWidth(null);
//        int originalHeight1 = originalImage1.getHeight(null);
//        int newWidth1 = 100;
//        int newHeight1 = (int) Math.round((double) newWidth1 / originalWidth1 * originalHeight1);
//        Image resizedImage1 = originalImage.getScaledInstance(newWidth, newHeight1, Image.SCALE_SMOOTH);
//        ImageIcon resizedIcon1 = new ImageIcon(resizedImage1);
//        latteImg.setIcon(resizedIcon1);
//        latteImg.setText("");

        setLocationRelativeTo(null);
        buttonByAmericano.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseHelper.getConnection()) {
                    assert connection != null;

                    String insertSql = "INSERT INTO order_items (item_id, amount, total_price, paid, completed) VALUES (1, 1, 3.5, 0, 0)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Execute the SQL statement
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

                    String insertSql = "INSERT INTO order_items (item_id, amount, total_price, paid, completed) VALUES (1, 1, 4, 0, 0)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Execute the SQL statement
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

                    String insertSql = "INSERT INTO order_items (item_id, amount, total_price, paid, completed) VALUES (1, 1, 4.5, 0, 0)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Execute the SQL statement
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

                    String insertSql = "INSERT INTO order_items (item_id, amount, total_price, paid, completed) VALUES (1, 1, 3, 0, 0)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Execute the SQL statement
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
    }

    public static void main(String[] args) {
        new OrderPage();
    }
}
