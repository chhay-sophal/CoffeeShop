import database.DatabaseHelper;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateEmployeeClass extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField usernameTextField;
    private JComboBox empTypeComboBox;
    private JTextField passwordTextField;
    private final ItemCreatedListener itemCreatedListener;
    private final int empID;

    public UpdateEmployeeClass(ItemCreatedListener listener, int id, String username, String password, String empType) {
        this.itemCreatedListener = listener;
        this.usernameTextField.setText(username);
        this.empTypeComboBox.setSelectedItem(empType);
        this.passwordTextField.setText(password);
        this.empID = id;
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
    }

    private void onOK() {
        // add your code here
        String username = usernameTextField.getText();
        String empType = (String) empTypeComboBox.getSelectedItem();
        String password = passwordTextField.getText();

        int empTypeID;
        // Update data in the database
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;

            if (Objects.equals(empType, "Administrator")) {
                empTypeID = 1;
            } else {
                empTypeID = 2;
            }

            // Prepare the SQL statement for updating
            String updateSql = "UPDATE users SET username = ?, password =?, user_type = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
                // Set parameters for the prepared statement
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setInt(3, empTypeID);
                preparedStatement.setInt(4, this.empID);

                // Execute the SQL statement
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Employee modified successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Notify the listener when a menu is modified
                    if (itemCreatedListener != null) {
                        itemCreatedListener.onItemCreated();
                    }
                    dispose(); // Close the dialog after successful update
                } else {
                    JOptionPane.showMessageDialog(this, "Employee with ID " + empID + " not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            Logger logger = Logger.getLogger(Dashboard.class.getName());
            logger.log(Level.SEVERE, "Error modifying menu", e);
            JOptionPane.showMessageDialog(this, "Error modifying employee", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
