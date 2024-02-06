import database.DatabaseHelper;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateCustomerClass extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPasswordField passwordFieldOld;
    private JPasswordField passwordFieldNew;
    private JPasswordField passwordFieldConfirm;
    private final ItemCreatedListener itemCreatedListener;
    private final String oldPassword;
    private final int userID;

    public UpdateCustomerClass(ItemCreatedListener listener, int userID, String oldPassword) {
        this.itemCreatedListener = listener;
        this.userID = userID;
        this.oldPassword = oldPassword;
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
        char[] newPassword = this.passwordFieldNew.getPassword();
        char[] confirmPassword = this.passwordFieldConfirm.getPassword();
        String oldPasswordString = new String(this.passwordFieldOld.getPassword());
        String newPasswordString = new String(newPassword);

        if (!Objects.equals(this.oldPassword, oldPasswordString)) {
            JOptionPane.showMessageDialog(null, "Old password is incorrect!");
        }
        else if (!Arrays.equals(newPassword, confirmPassword)) {
            JOptionPane.showMessageDialog(null, "New passwords do not match!");
        }
        else {
            try (Connection connection = DatabaseHelper.getConnection()) {
                assert connection != null;

                // Prepare the SQL statement for updating
                String updateSql = "UPDATE users SET password = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
                    // Set parameters for the prepared statement
                    preparedStatement.setString(1, newPasswordString);
                    preparedStatement.setInt(2, this.userID);

                    // Execute the SQL statement
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Employee modified successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        // Notify the listener when a menu is modified
                        if (itemCreatedListener != null) {
                            itemCreatedListener.onItemCreated();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee with ID " + userID + " not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                Logger logger = Logger.getLogger(Dashboard.class.getName());
                logger.log(Level.SEVERE, "Error modifying menu", e);
                JOptionPane.showMessageDialog(this, "Error modifying employee", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
