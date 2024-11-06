package medicalrecords;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TreatmentEntryHandler {

    // Method to save treatment entry to the database
    public static void saveTreatmentEntry(int studentId, String symptoms, String diagnosis, String treatmentGiven, String followUpNotes) {
        String query = "INSERT INTO treatment_history (student_id, symptoms, diagnosis, treatment_given, follow_up_notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setString(2, symptoms);
            statement.setString(3, diagnosis);
            statement.setString(4, treatmentGiven);
            statement.setString(5, followUpNotes);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get treatment history for a specific patient
    public static ResultSet getPatientHistory(int studentId) {
        String query = "SELECT * FROM treatment_history WHERE student_id = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            return statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get today's treatment entries
    public static ResultSet getDailyEntries() {
        String query = "SELECT * FROM treatment_history WHERE date = CURDATE()";
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
