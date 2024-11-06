package medicalrecords;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminPage extends JFrame {
    private JTable treatmentTable;
    private DefaultTableModel tableModel;

    public AdminPage() {
        setTitle("Admin Page");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set background color for main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Header label
        JLabel headerLabel = new JLabel("Treatment Entries", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(33, 150, 243)); // Light blue text
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Table setup with customized appearance
        treatmentTable = new JTable();
        tableModel = new DefaultTableModel(
            new Object[]{"Student ID", "Date", "Symptoms", "Diagnosis", "Treatment Given", "Follow-up Notes"}, 0
        );
        treatmentTable.setModel(tableModel);
        treatmentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        treatmentTable.setRowHeight(25);
        treatmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        treatmentTable.getTableHeader().setBackground(new Color(33, 150, 243));
        treatmentTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(treatmentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Button for loading treatment entries with styling
        JButton loadEntriesButton = new JButton("Load Treatment Entries");
        loadEntriesButton.setFont(new Font("Arial", Font.BOLD, 16));
        loadEntriesButton.setBackground(new Color(33, 150, 243));
        loadEntriesButton.setForeground(Color.WHITE);
        loadEntriesButton.setFocusPainted(false);
        loadEntriesButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loadEntriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEntries();
            }
        });

        // Panel for button with centered alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(loadEntriesButton);

        // Add components to main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // Method to load entries from treatment_history table
    public void loadEntries() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT student_id, date, symptoms, diagnosis, treatment_given, follow_up_notes FROM treatment_history";
            ResultSet rs = stmt.executeQuery(query);
            
            // Process each row in result set
            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String date = rs.getString("date");
                String symptoms = rs.getString("symptoms");
                String diagnosis = rs.getString("diagnosis");
                String treatmentGiven = rs.getString("treatment_given") != null ? rs.getString("treatment_given") : "N/A";
                String followUpNotes = rs.getString("follow_up_notes") != null ? rs.getString("follow_up_notes") : "N/A";
                
                // Add row to table model
                tableModel.addRow(new Object[]{studentId, date, symptoms, diagnosis, treatmentGiven, followUpNotes});
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading treatment entries", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AdminPage();
    }
}
