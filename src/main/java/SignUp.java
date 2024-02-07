import database.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUp extends JFrame {
    private JPanel signUpPanel;
    private JLabel signupImg;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JPasswordField passwordFieldConfirm;
    private JButton signUpButton;

    public SignUp() {
        // Initialize
        setContentPane(signUpPanel);
        setTitle("Dashboard");
        setSize(1000, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set image
        ImageIcon originalIcon = new ImageIcon("images/signup.png");
        Image originalImage = originalIcon.getImage();
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);
        int newWidth = 400;
        int newHeight = (int) Math.round((double) newWidth / originalWidth * originalHeight);
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        signupImg.setIcon(resizedIcon);
        signupImg.setText("");

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textFieldUsername.getText();
                char[] password = passwordField.getPassword();
                char[] confirmPassword = passwordFieldConfirm.getPassword();

                if (!passwordMatches(password, confirmPassword)) {
                    JOptionPane.showMessageDialog(signUpPanel, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method if passwords don't match
                }

                // Hash the password (use a secure method in a real application)
                String hashedPassword = new String(password);

                // Insert data into the database
                try (Connection connection = DatabaseHelper.getConnection()) {
                    assert connection != null;

                    String insertSql = "INSERT INTO users (username, password, user_type) VALUES (?, ?, ?)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        // Set parameters for the prepared statement
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, hashedPassword);
                        preparedStatement.setInt(3, 3);

                        // Execute the SQL statement
                        preparedStatement.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(signUpPanel, "Customer created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                    new LogInPage();

                    dispose();
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

    private boolean passwordMatches(char[] password, char[] confirmPassword) {
        return new String(password).equals(new String(confirmPassword));
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
