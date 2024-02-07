import javax.swing.*;
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

                    // Validate text field to not null
                    if (!inputUsername.isEmpty() &&!inputPassword.isEmpty()) {
                        //print input values
                        System.out.println("Input username :" + inputUsername);
                        System.out.println("Input password :" +inputPassword);

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
                                        //Navigate to Dashboard
                                        Dashboard d = new Dashboard();
                                        d.showForm();
                                    } else{
                                        //Navigate to HomePage
                                        HomePage h = new HomePage();
                                        h.showForm();
                                        // Valid login: Implement your logic here (e.g., open a new window)
                                        System.out.println("Login successful!");
                                        break; // Exit the loop since a valid login is found
                                    }

                                }else{
                                    JOptionPane.showMessageDialog(null, "Invalid username or Password!", "Error", JOptionPane.ERROR_MESSAGE);

                                }
                            }
                            if (rowCount == 0) {
                                JOptionPane.showMessageDialog(null, "Invalid username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Input username or password cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

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
