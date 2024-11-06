package medicalrecords;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class DailyEntriesPage extends JFrame {
    private JTextArea entriesArea;

    public DailyEntriesPage() {
        setTitle("Daily Entries");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        entriesArea = new JTextArea();
        entriesArea.setEditable(false);
        add(new JScrollPane(entriesArea), BorderLayout.CENTER);

        // Load today's entries
        loadDailyEntries();
    }

    private void loadDailyEntries() {
        ResultSet resultSet = TreatmentEntryHandler.getDailyEntries();
        entriesArea.setText(""); // Clear previous entries

        try {
            while (resultSet.next()) {
                String entry = "Date: " + resultSet.getDate("date") +
                               "\nStudent ID: " + resultSet.getInt("student_id") +
                               "\nSymptoms: " + resultSet.getString("symptoms") +
                               "\nDiagnosis: " + resultSet.getString("diagnosis") +
                               "\nTreatment Given: " + resultSet.getString("treatment_given") +
                               "\nFollow-up Notes: " + resultSet.getString("follow_up_notes") +
                               "\n\n";
                entriesArea.append(entry);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error retrieving entries: " + ex.getMessage());
        }
    }
}
