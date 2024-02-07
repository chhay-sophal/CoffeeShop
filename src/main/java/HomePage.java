
import Product.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

public class HomePage extends JFrame {
    private JPanel panel1;
    private JLabel NameLabel;
    private JLabel PriceLabel;
    private JLabel PictureLabel;
    private JPanel OrderPanel;
    private JButton orderButton;
    private JButton deleteButton1;
    private JButton cancelButton;
    private JTable OrderTable;
    private JLabel image;
    private JTable table2;
    private JLabel totalPriceLabel;

    public HomePage() {
        ImageIcon Image = new ImageIcon("images/logo.png");
        image.setIcon(Image);
        ArrayList<Product> productList = new ArrayList<>();

        // Create instances of Coffee
        Coffee[] coffees = {
                new Coffee("Espresso", "path/to/espresso_image.jpg", 2.5),
                new Coffee("Latte", "path/to/latte_image.jpg", 3.0),
                new Coffee("Cappuccino", "path/to/cappuccino_image.jpg", 3.5),
                new Coffee("Americano", "path/to/americano_image.jpg", 2.0),
                new Coffee("Mocha", "path/to/mocha_image.jpg", 4.0),
                new Coffee("Macchiato", "path/to/macchiato_image.jpg", 3.2)
                // Add more Coffee instances here as needed
        };

        // Create a table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Price");

        // Add data to the table model
        for (Coffee coffee : coffees) {
            model.addRow(new Object[]{coffee.getName(), coffee.getPrice()});
        }

        // Set the table model to table2
        table2.setModel(model);
        table2.setRowHeight(30);
        table2.setShowGrid(false);
        table2.setIntercellSpacing(new Dimension(0, 0));
//        table2.setRowSelectionAllowed(false);
//        table2.setColumnSelectionAllowed(false);
///////////////////////////////////////////////////////////////////


        table2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if the selection is not adjusting
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table2.getSelectedRow();
                    int selectedColumn = table2.getSelectedColumn();
                    Object nameObj = table2.getValueAt(selectedRow, 0); // Extract name from column 0
                    Object priceObj = table2.getValueAt(selectedRow, 1);

                    String name = nameObj.toString(); // Convert to String
                    double price = Double.parseDouble(priceObj.toString()); // Convert to double
                    Product product = new Product(name, price);

                    if (selectedRow != -1 && selectedColumn != -1) {
                        DefaultTableModel modelorder = new DefaultTableModel(new Object[]{"Name", "Price"}, 0);

                        Object value = table2.getValueAt(selectedRow, selectedColumn);

                        int option = JOptionPane.showConfirmDialog(table2, "Do you want to buy " + value + "?"+nameObj+priceObj, "Buy Confirmation", JOptionPane.YES_NO_OPTION);

                        if (option == JOptionPane.YES_OPTION) {
                            // Buy action

                            JOptionPane.showMessageDialog(table2, "You bought " + value + ".", "Purchase Success", JOptionPane.INFORMATION_MESSAGE);
                            productList.add(product);

                        } else {
                            // Cancel action
                            JOptionPane.showMessageDialog(table2, "You cancelled the purchase.", "Purchase Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        }


                        /////////Table order

                        for (Product product1 : productList) {
                            modelorder.addRow(new Object[]{product1.getName(), product1.getPrice()});
                        }
                        OrderTable.setModel(modelorder);
                        // Create a label to display total price
//                        JLabel totalPriceLabel = new JLabel("Total Price: ");

                        // Calculate and update total price label
                        updateTotalPriceLabel(totalPriceLabel, OrderTable, 1); // A
                        deleteButton1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int selectedRow = OrderTable.getSelectedRow();
                                if (selectedRow != -1) {
                                    modelorder.removeRow(selectedRow);
                                    JOptionPane.showMessageDialog(OrderTable, "Item deleted successfully.", "Delete Success", JOptionPane.INFORMATION_MESSAGE);

                                } else {
                                }
                            }
                        });

                        orderButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JOptionPane.showMessageDialog(OrderTable, "Order placed successfully!", "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
                                // Clear the table or perform any other necessary actions after placing the order
                                modelorder.setRowCount(0); // Clear all rows from the order table
                            }
                        });

                        cancelButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Implement the action to be performed when the cancel button is clicked
                                // This could involve clearing the order table, resetting any form fields, etc.
                                // For example:
                                modelorder.setRowCount(0); // Clear all rows from the order table
                                JOptionPane.showMessageDialog(OrderTable, "Order cancelled.", "Cancellation", JOptionPane.INFORMATION_MESSAGE);
                            }
                        });

                    }


                }

            }
        });



    }
//    private void deleteOrder() {
////        DefaultTableModel modelorder = new DefaultTableModel(new Object[]{"Name", "Price"}, 0);
//
//        int selectedRow = OrderTable.getSelectedRow();
//        if (selectedRow != -1) {
//            modelorder.removeRow(selectedRow);
//        } else {
//            JOptionPane.showMessageDialog(this, "Please select an order to delete.", "No Selection", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    // Method to calculate and update total price label
    private static void updateTotalPriceLabel(JLabel label, JTable table, int columnIndex) {
        double total = 0.0;
        TableModel model = table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            // Convert the price String to Double before adding to total
            try {
                double price = Double.parseDouble(model.getValueAt(i, columnIndex).toString());
                total += price;
            } catch (NumberFormatException e) {
                // Handle invalid or empty price values
                System.err.println("Invalid price value at row " + i);
            }
        }
        label.setText("Total Price: " + total);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            HomePage homePage = new HomePage();
            homePage.setContentPane(homePage.panel1);
//            homePage.getContentPane().add(label);
            homePage.setTitle("HomePage");
            homePage.setSize(1000, 600);
            homePage.setVisible(true);
            homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
