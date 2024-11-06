package medicalrecords;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEntryPage extends JFrame {
    private JTextField studentIdField;
    private JTextField symptomsField;
    private JTextField diagnosisField;
    private JTextField treatmentGivenField;
    private JTextField dateField; // Format: dd-MM-yyyy
    private JButton saveButton;

    public AddEntryPage() {
        setTitle("Add Entry");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Input fields
        studentIdField = new JTextField(15);
        symptomsField = new JTextField(15);
        diagnosisField = new JTextField(15);
        treatmentGivenField = new JTextField(15);
        dateField = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()), 10);

        // Save button
        saveButton = new JButton("Save Entry");
        saveButton.addActionListener(this::saveEntry);

        // Layout
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        panel.add(new JLabel("Symptoms:"));
        panel.add(symptomsField);
        panel.add(new JLabel("Diagnosis:"));
        panel.add(diagnosisField);
        panel.add(new JLabel("Treatment Given:"));
        panel.add(treatmentGivenField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(saveButton);

        add(panel);
    }

    private void saveEntry(ActionEvent event) {
        String studentId = studentIdField.getText();
        String symptoms = symptomsField.getText();
        String diagnosis = diagnosisField.getText();
        String treatmentGiven = treatmentGivenField.getText();
        String dateInput = dateField.getText();

        // Validate inputs
        if (studentId.isEmpty() || symptoms.isEmpty() || diagnosis.isEmpty() || treatmentGiven.isEmpty() || dateInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert date from dd-MM-yyyy to yyyy-MM-dd
        String formattedDate;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateInput);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd-MM-yyyy.", "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // SQL to insert entry
        String sql = "INSERT INTO treatment_history (student_id, symptoms, diagnosis, treatment_given, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, symptoms);
            pstmt.setString(3, diagnosis);
            pstmt.setString(4, treatmentGiven);
            pstmt.setString(5, formattedDate); // Use the correctly formatted date
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Entry added successfully.");
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding entry: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        studentIdField.setText("");
        symptomsField.setText("");
        diagnosisField.setText("");
        treatmentGivenField.setText("");
        dateField.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date())); // Reset date field
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddEntryPage addEntryPage = new AddEntryPage();
            addEntryPage.setVisible(true);
        });
    }
}
