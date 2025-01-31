import database.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class LogInPage extends JFrame {
    private JPanel panel1;
    private JButton loginButton;
    private JPasswordField passwordPasswordField;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JLabel loginImg;
    private JButton signUPButton;
    private JTextPane textPane1;
    public int userID;

    private void DebugEmployeeTbl(){
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE user_type IN (1, 2)")) {

                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    // Print or log the row information
                    System.out.println("Username: " + username + ", Password: " + password);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadedHomePage(){

    }
    public LogInPage() {
        // Initialize
        DebugEmployeeTbl();
        setContentPane(panel1);
        setTitle("Login");
        setSize(1000,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set image
        ImageIcon originalIcon = new ImageIcon("src/main/java/images/login2.png");
        Image originalImage = originalIcon.getImage();
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);
        int newWidth = 400;
        int newHeight = (int) Math.round((double) newWidth / originalWidth * originalHeight);
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        loginImg.setIcon(resizedIcon);
        loginImg.setText("");

        //Apply login action to the login btn
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseHelper.getConnection()) {
                    assert connection != null;

                    //Get input Username
                    String inputUsername = textFieldUsername.getText();

                    //Get input UserPassword
                    char[] arrPassword = passwordField.getPassword();
                    String inputPassword = new String(arrPassword);

                    // Validate text field to not null
                    if (!inputUsername.isEmpty() && !inputPassword.isEmpty()) {
                        // Print input values
                        System.out.println("Input username: " + inputUsername);
                        System.out.println("Input password: " + inputPassword);

                        try (Statement statement = connection.createStatement();
                             ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username = '" + inputUsername + "'")) {

                            int rowCount = 0;
                            boolean validLogin = false;

                            while (resultSet.next()) {
                                rowCount++;

                                String storedPassword = resultSet.getString("password");
                                String storedUsername = resultSet.getString("username");
                                userID = resultSet.getInt("id");
                                int userType = resultSet.getInt("user_type");

                                // Set admin username and password
                                String userAdmin = "Admin";
                                String passwordAdmin = "adminComplexPsw123";

                                if (storedPassword.equals(inputPassword) && storedUsername.equals(inputUsername)) {
                                    if (inputUsername.equals(userAdmin) && inputPassword.equals(passwordAdmin)) {
                                        System.out.println("Login as default Admin");
                                    } else if (userType == 1) {
                                        new Dashboard();
                                        System.out.println("Login as admin user");
                                        dispose();
                                    } else if (userType == 2) {
                                        new OrderManagement();
                                        System.out.println("Login as staff user");
                                        dispose();
                                    }else {
                                        new OrderPage(userID);
                                        System.out.println("Login as normal user");
                                        dispose();
                                    }

                                    // Valid login: Implement your logic here (e.g., open a new window)
                                    System.out.println("Login successful!");
                                    validLogin = true;
                                    break; // Exit the loop since a valid login is found
                                }
                            }

                            if (!validLogin) {
                                JOptionPane.showMessageDialog(null, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace(); // Handle or log the SQLException
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Input username or password cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }



                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        signUPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUp();
                dispose();
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        new LogInPage();
    }
}
