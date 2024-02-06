import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LogInPage extends JFrame {
    private JPanel panel1;
    private JButton loginButton;
    private JPasswordField passwordPasswordField;
    private JTextPane textPane1;


    private void DebugEmployeeTbl(){
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM employees")) {

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
    public LogInPage() {

        //Apply login action to the login btn
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseHelper.getConnection()) {
                    assert connection != null;

                    //Get input Username
                    String inputUsername = textPane1.getText();

                    //Get input UserPassword
                    char[] arrPassword = passwordPasswordField.getPassword();
                    String inputPassword = new String(arrPassword);

                    System.out.println(inputUsername);
                    System.out.println(inputPassword);


                    try (Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery("SELECT * FROM employees WHERE username = '" + inputUsername + "'")) {

                        int rowCount = 0;
                        while (resultSet.next()) {
                            rowCount++;

                            String storedPassword = resultSet.getString("password");
                            String storedUsername = resultSet.getString("username");

                            //set admin username and password
                            String userAdmin = "Admin";
                            String passwordAdmin = "adminComplexPsw123";

                            if (storedPassword.equals(inputPassword) && storedUsername.equals(inputUsername)) {
                                if (inputUsername.equals(userAdmin) && inputPassword.equals(passwordAdmin)){
                                    //Navigate to admin page
                                } else{
                                    //Navigate to user page
                                }
                                // Valid login: Implement your logic here (e.g., open a new window)
                                System.out.println("Login successful!");
                                break; // Exit the loop since a valid login is found
                            }
                            
                        }

                        if (rowCount == 0) {
                            // Invalid username
                            System.out.println("Invalid username!");

                        } else if (rowCount > 0) {
                            // Invalid password
                            System.out.println("Invalid password!");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
//      int getRowCount(ResultSet resultSet) throws SQLException {
//        int rowCount = 0;
//        while (resultSet.next()) {
//            rowCount++;
//        }
//        return rowCount;
//    }

    public static void main(String[] args) throws SQLException {

        LogInPage logInPage = new LogInPage();
        logInPage.DebugEmployeeTbl();

        logInPage.setContentPane(logInPage.panel1);
        logInPage.setTitle("Login");
        logInPage.setSize(500,300);
        logInPage.setVisible(true);
        logInPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
