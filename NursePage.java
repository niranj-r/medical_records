package medicalrecords;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NursePage extends JFrame {
    private JTextField studentIdField;
    private JTextField symptomsField;
    private JTextField diagnosisField;
    private JTextField treatmentGivenField;
    private JTextField dateField;
    private JButton addEntryButton;
    private JButton viewPatientHistoryButton;
    private JButton viewDailyEntriesButton;
    private JTable treatmentTable;
    private DefaultTableModel tableModel;

    public NursePage() {
        setTitle("Nurse Page - Medical Records");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Setting up colors
        Color primaryColor = new Color(60, 130, 180);
        Color secondaryColor = new Color(240, 240, 255);
        Color buttonColor = new Color(80, 160, 220);
        Color textColor = Color.WHITE;

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        JLabel headerLabel = new JLabel("Medical Records - Nurse Page");
        headerLabel.setForeground(textColor);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(secondaryColor);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Student ID:", JLabel.RIGHT));
        studentIdField = new JTextField(15);
        inputPanel.add(studentIdField);

        inputPanel.add(new JLabel("Symptoms:", JLabel.RIGHT));
        symptomsField = new JTextField(15);
        inputPanel.add(symptomsField);

        inputPanel.add(new JLabel("Diagnosis:", JLabel.RIGHT));
        diagnosisField = new JTextField(15);
        inputPanel.add(diagnosisField);

        inputPanel.add(new JLabel("Treatment Given:", JLabel.RIGHT));
        treatmentGivenField = new JTextField(15);
        inputPanel.add(treatmentGivenField);

        inputPanel.add(new JLabel("Date (dd-MM-yyyy):", JLabel.RIGHT));
        dateField = new JTextField(new SimpleDateFormat("dd-MM-yyyy").format(new Date()), 10);
        inputPanel.add(dateField);

        add(inputPanel, BorderLayout.WEST);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBackground(primaryColor);

        addEntryButton = createButton("Add Entry", buttonColor, textColor);
        addEntryButton.addActionListener(this::addEntry);

        viewPatientHistoryButton = createButton("View Patient History", buttonColor, textColor);
        viewPatientHistoryButton.addActionListener(e -> loadPatientHistory());

        viewDailyEntriesButton = createButton("View Daily Entries", buttonColor, textColor);
        viewDailyEntriesButton.addActionListener(e -> loadDailyEntries());

        buttonPanel.add(addEntryButton);
        buttonPanel.add(viewPatientHistoryButton);
        buttonPanel.add(viewDailyEntriesButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Table setup for displaying treatment history
        tableModel = new DefaultTableModel(new String[]{"Student ID", "Symptoms", "Diagnosis", "Treatment Given", "Date"}, 0);
        treatmentTable = new JTable(tableModel);
        treatmentTable.setBackground(Color.WHITE);
        treatmentTable.setGridColor(primaryColor);

        JScrollPane scrollPane = new JScrollPane(treatmentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Treatment History"));
        add(scrollPane, BorderLayout.CENTER);
    }

    // Utility method to create styled buttons
    private JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void addEntry(ActionEvent event) {
        String studentId = studentIdField.getText();
        String symptoms = symptomsField.getText();
        String diagnosis = diagnosisField.getText();
        String treatmentGiven = treatmentGivenField.getText();
        String date = dateField.getText();

        if (studentId.isEmpty() || symptoms.isEmpty() || diagnosis.isEmpty() || treatmentGiven.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO treatment_history (student_id, symptoms, diagnosis, treatment_given, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, symptoms);
            pstmt.setString(3, diagnosis);
            pstmt.setString(4, treatmentGiven);
            pstmt.setString(5, date);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Entry added successfully.");
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding entry: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPatientHistory() {
        String studentId = studentIdField.getText();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID to view history.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM treatment_history WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0); // Clear previous data
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("student_id"),
                        rs.getString("symptoms"),
                        rs.getString("diagnosis"),
                        rs.getString("treatment_given"),
                        rs.getString("date")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patient history: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDailyEntries() {
        String date = dateField.getText();
        String sql = "SELECT * FROM treatment_history WHERE date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0); // Clear previous data
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("student_id"),
                        rs.getString("symptoms"),
                        rs.getString("diagnosis"),
                        rs.getString("treatment_given"),
                        rs.getString("date")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading daily entries: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
            NursePage nursePage = new NursePage();
            nursePage.setVisible(true);
        });
    }
}
