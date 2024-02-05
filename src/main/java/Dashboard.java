import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.SQLException;
import java.util.Vector;

public class Dashboard extends JFrame {
    private static final String APPLICATIONS_FILE = "config.properties";
    private JPanel dashboardPanel;
    private JTabbedPane tabbedPane1;
    private JButton createAStaffAccountButton;
    private JTable tableStaff;
    private DefaultTableModel staffTableModel;

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

        // Initialize table model and set it to the table
//        staffTableModel = new DefaultTableModel();
        String[] columnNames = {"ID", "Username",};
        staffTableModel = new DefaultTableModel(columnNames, 0);
        System.out.print(staffTableModel.getColumnName(1));
        tableStaff.setModel(staffTableModel);
        // Fetch staff data from the database
        fetchStaffData();
    }

    private void fetchStaffData() {
        try (Connection connection = DatabaseHelper.getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM users where user_type = 1")) {
                while (resultSet.next()) {
                        Vector<Object> row = new Vector<>();
                        row.add(resultSet.getInt("id"));
                        row.add(resultSet.getString("username"));
                        // Add more columns as needed

                        staffTableModel.addRow(row);
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
