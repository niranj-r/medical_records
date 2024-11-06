package medicalrecords;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentPage extends JFrame {
    private JTextField studentIdField;
    private JButton viewRecordsButton;
    private JTable recordsTable;
    private DefaultTableModel tableModel;

    // Constructor now takes studentId as an argument
    public StudentPage(String studentId) {
        setTitle("Student Medical Records");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set custom font and color for header
        JLabel headerLabel = new JLabel("Student Medical Records", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204));
        headerLabel.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Input field for student ID
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        studentIdField = new JTextField(studentId, 15);
        studentIdField.setEditable(false);
        studentIdField.setBackground(new Color(240, 240, 240));
        
        // Customize button style
        viewRecordsButton = new JButton("View Records");
        viewRecordsButton.setBackground(new Color(0, 102, 204));
        viewRecordsButton.setForeground(Color.WHITE);
        viewRecordsButton.setFocusPainted(false);
        viewRecordsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewRecordsButton.addActionListener(this::viewRecords);

        // Table to display records
        String[] columnNames = {"Date", "Symptoms", "Diagnosis", "Treatment Given"};
        tableModel = new DefaultTableModel(columnNames, 0);
        recordsTable = new JTable(tableModel);
        recordsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        recordsTable.setRowHeight(25);
        recordsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        recordsTable.getTableHeader().setBackground(new Color(230, 230, 250));
        recordsTable.getTableHeader().setForeground(new Color(0, 102, 204));
        
        JScrollPane scrollPane = new JScrollPane(recordsTable);

        // Layout setup
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        inputPanel.add(studentIdLabel);
        inputPanel.add(studentIdField);
        inputPanel.add(viewRecordsButton);

        add(headerLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Load records for the given student ID
        loadRecords(studentId);
    }

    private void loadRecords(String studentId) {
        // Clear previous records
        tableModel.setRowCount(0);

        // SQL query to retrieve medical records
        String sql = "SELECT date, symptoms, diagnosis, treatment_given FROM treatment_history WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                String symptoms = rs.getString("symptoms");
                String diagnosis = rs.getString("diagnosis");
                String treatmentGiven = rs.getString("treatment_given");
                tableModel.addRow(new Object[]{date, symptoms, diagnosis, treatmentGiven});
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No records found for Student ID: " + studentId, "No Records", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method for button action
    public void viewRecords(ActionEvent e) {
        // Reload the records if needed
        String studentId = studentIdField.getText();
        loadRecords(studentId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For testing without login, pass an example student ID
            StudentPage studentPage = new StudentPage("12345");
            studentPage.setVisible(true);
        });
    }
}
