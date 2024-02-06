import database.DatabaseHelper;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateMenuClass extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JTextField textFieldPrice;
    private final ItemCreatedListener itemCreatedListener;
    private final int menuID;

    public UpdateMenuClass(ItemCreatedListener listener, int id, String textFieldName, String textFieldPrice) {
        this.itemCreatedListener = listener;
        this.menuID = id;
        System.out.print(textFieldName);
        this.textFieldName.setText(textFieldName);
        this.textFieldPrice.setText(textFieldPrice);

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
        String name = textFieldName.getText();
        double price = Double.parseDouble(textFieldPrice.getText());

        // Update data in the database
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;

            // Prepare the SQL statement for updating
            String updateSql = "UPDATE menu SET name = ?, price = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
                // Set parameters for the prepared statement
                preparedStatement.setString(1, name);
                preparedStatement.setDouble(2, price);
                preparedStatement.setInt(3, this.menuID);

                // Execute the SQL statement
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Menu modified successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Notify the listener when a menu is modified
                    if (itemCreatedListener != null) {
                        itemCreatedListener.onItemCreated();
                    }
                    dispose(); // Close the dialog after successful update
                } else {
                    JOptionPane.showMessageDialog(this, "Menu with ID " + menuID + " not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            Logger logger = Logger.getLogger(Dashboard.class.getName());
            logger.log(Level.SEVERE, "Error modifying menu", e);
            JOptionPane.showMessageDialog(this, "Error modifying menu", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
