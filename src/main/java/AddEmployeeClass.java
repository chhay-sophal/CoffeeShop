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
    private JTextField textFieldPassword;
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
        String password = textFieldPassword.getText();

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
                preparedStatement.setString(2, password);
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
