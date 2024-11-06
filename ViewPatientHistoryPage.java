package medicalrecords;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewPatientHistoryPage extends JFrame {
    private JTextField studentIdField;
    private JTextArea historyArea;
    private JButton viewHistoryButton;

    public ViewPatientHistoryPage() {
        setTitle("View Patient History");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdField = new JTextField();
        historyArea = new JTextArea();
        viewHistoryButton = new JButton("View History");

        studentIdLabel.setBounds(20, 20, 100, 30);
        studentIdField.setBounds(150, 20, 200, 30);
        viewHistoryButton.setBounds(150, 60, 200, 30);
        historyArea.setBounds(20, 100, 360, 150);
        historyArea.setEditable(false);

        add(studentIdLabel);
        add(studentIdField);
        add(viewHistoryButton);
        add(historyArea);

        viewHistoryButton.addActionListener(e -> loadPatientHistory());
    }

    private void loadPatientHistory() {
        String studentId = studentIdField.getText();
        String query = "SELECT * FROM treatment_history WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            historyArea.setText(""); // Clear previous history

            while (rs.next()) {
                String symptoms = rs.getString("symptoms");
                String diagnosis = rs.getString("diagnosis");
                String treatment = rs.getString("treatment");
                historyArea.append("Symptoms: " + symptoms + "\nDiagnosis: " + diagnosis + "\nTreatment: " + treatment + "\n\n");
            }

            if (historyArea.getText().isEmpty()) {
                historyArea.setText("No records found for Student ID: " + studentId);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewPatientHistoryPage().setVisible(true);
    }
}
