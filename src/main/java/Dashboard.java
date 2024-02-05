import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.SQLException;

public class Dashboard extends JFrame {
    private static final String APPLICATIONS_FILE = "config.properties";
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane1;
    private JButton createAStaffAccountButton;
    private JTable tableStaff;

    public Dashboard() {

        createAStaffAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the dialog when the button is clicked
                AddStaffClass dialog = new AddStaffClass();
                dialog.setSize(500,300);
                dialog.setVisible(true);
            }
        });
    }

    private static int getRowCount(ResultSet resultSet) throws SQLException {
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        return rowCount;
    }

    public static void main(String[] args) throws SQLException {
        Dashboard dashboard = new Dashboard();
        dashboard.setContentPane(dashboard.dashboardPanel);
        dashboard.setTitle("Dashboard");
        dashboard.setSize(500,300);
        dashboard.setVisible(true);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
