import database.DatabaseHelper;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddEmployeeClass extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JPasswordField passwordFieldConfirm;
    private JComboBox comboBoxUserType;
    private ItemCreatedListener itemCreatedListener;

    public AddEmployeeClass(ItemCreatedListener listener) {
        this.itemCreatedListener = listener;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void onOK() {
        // add your code here
        String username = textFieldUsername.getText();
        char[] password = passwordField.getPassword();
        char[] confirmPassword = passwordFieldConfirm.getPassword();
        // Check if passwords match
        if (!passwordMatches(password, confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if passwords don't match
        }

        // Hash the password (use a secure method in a real application)
        String hashedPassword = new String(password);

        // Check user type
        int userType;
        Object selectedUserType = this.comboBoxUserType.getSelectedItem();

        if (selectedUserType != null && selectedUserType.toString().equals("Administrator")) {
            userType = 1;
        } else {
            userType = 2;
        }

        // Insert data into the database
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;

            // Prepare the SQL statement for insertion
            String insertSql = "INSERT INTO users (username, password, user_type) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                // Set parameters for the prepared statement
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setInt(3, userType);

                // Execute the SQL statement
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Employee created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Notify the listener when a user is created
            if (itemCreatedListener != null) {
                itemCreatedListener.onItemCreated();
            }
            dispose(); // Close the dialog after successful insertion
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating user", "Error", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }

    private boolean passwordMatches(char[] password, char[] confirmPassword) {
        // Implement password matching logic
        // For simplicity, compare as strings in this example
        return new String(password).equals(new String(confirmPassword));
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
//        AddEmployeeClass dialog = new AddEmployeeClass();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
    }
}
