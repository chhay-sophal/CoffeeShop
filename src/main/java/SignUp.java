import javax.swing.*;
import java.awt.*;

public class SignUp extends JFrame {
    private JPanel signUpPanel;
    private JLabel signupImg;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JPasswordField passwordFieldConfirm;
    private JButton signUpButton;

    public SignUp() {
        setContentPane(signUpPanel);
        setTitle("Dashboard");
        setSize(1000, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set image
        ImageIcon originalIcon = new ImageIcon("images/signup.png");
        Image originalImage = originalIcon.getImage();
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);
        int newWidth = 400;
        int newHeight = (int) Math.round((double) newWidth / originalWidth * originalHeight);
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        signupImg.setIcon(resizedIcon);
        signupImg.setText("");
    }

    public static void main(String[] args) {
        new SignUp();
    }
}
