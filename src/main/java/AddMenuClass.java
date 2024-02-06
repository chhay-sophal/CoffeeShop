import database.DatabaseHelper;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMenuClass extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JTextField textFieldPrice;
    private final ItemCreatedListener itemCreatedListener;
    public AddMenuClass(ItemCreatedListener listener) {
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
    }

    private void onOK() {
        // add your code here
        String name = textFieldName.getText();
        double price = Double.parseDouble(textFieldPrice.getText());

        // Insert data into the database
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;

            // Prepare the SQL statement for insertion
            String insertSql = "INSERT INTO menu (name, price) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                // Set parameters for the prepared statement
                preparedStatement.setString(1, name);
                preparedStatement.setDouble(2, price);

                // Execute the SQL statement
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Menu created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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
    }
}
