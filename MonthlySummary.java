package medicalrecords;

import javax.swing.*;

public class MonthlySummary extends JFrame {
    public MonthlySummary() {
        setTitle("Monthly Summary");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Implement functionality to fetch and display monthly summaries
        JLabel label = new JLabel("Monthly Summary: Total Visits, Common Diagnoses");
        add(label);
    }
}
