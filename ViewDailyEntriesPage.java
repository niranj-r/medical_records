package medicalrecords;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewDailyEntriesPage extends JFrame {
    private JTextField dateField;
    private JTextArea entriesArea;
    private JButton viewEntriesButton;

    public ViewDailyEntriesPage() {
        setTitle("View Daily Entries");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateField = new JTextField();
        entriesArea = new JTextArea();
        viewEntriesButton = new JButton("View Entries");

        dateLabel.setBounds(20, 20, 150, 30);
        dateField.setBounds(170, 20, 200, 30);
        viewEntriesButton.setBounds(170, 60, 200, 30);
        entriesArea.setBounds(20, 100, 360, 150);
        entriesArea.setEditable(false);

        add(dateLabel);
        add(dateField);
        add(viewEntriesButton);
        add(entriesArea);

        viewEntriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDailyEntries();
            }
        });
    }

    private void loadDailyEntries() {
        String date = dateField.getText();
        String query = "SELECT * FROM treatment_history WHERE DATE(date_column) = ?"; // Use your date column name

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();

            entriesArea.setText(""); // Clear previous entries

            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String symptoms = rs.getString("symptoms");
                String diagnosis = rs.getString("diagnosis");
                String treatment = rs.getString("treatment");
                entriesArea.append("Student ID: " + studentId + "\nSymptoms: " + symptoms + "\nDiagnosis: " + diagnosis + "\nTreatment: " + treatment + "\n\n");
            }

            if (entriesArea.getText().isEmpty()) {
                entriesArea.setText("No entries found for Date: " + date);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading entries: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewDailyEntriesPage().setVisible(true);
    }
}
