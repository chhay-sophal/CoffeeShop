import javax.swing.*;

public class HomePage {
    private JPanel panel1;


    // Display form
    public void showForm() {
        // Create a JFrame instance
        JFrame frame = new JFrame("Form HomePage");

        // Set the content pane to the panel of the HomePage class
        frame.setContentPane(panel1);

        // Adjust the frame size and make it visible
        frame.pack();
        frame.setVisible(true);
    }
}
