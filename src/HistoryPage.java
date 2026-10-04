import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class HistoryPage extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel historyPanel;
    
    public HistoryPage() {
        setTitle("Workout History & Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel lblTitle = new JLabel("Previous Workout Settings", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Impact", Font.PLAIN, 32));
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        contentPane.add(lblTitle, BorderLayout.NORTH);
        
        // History panel
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(historyPanel);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Back to Home");
        btnBack.addActionListener(e -> goBackToHome());
        bottomPanel.add(btnBack);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        
        // Load history
        loadWorkoutHistory();
    }
    
    private void loadWorkoutHistory() {
        historyPanel.removeAll();
        
        try {
            String userEmail = Session.getCurrentUserEmail();
            if (userEmail == null) {
                JLabel noHistoryLabel = new JLabel("No user logged in", SwingConstants.CENTER);
                noHistoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
                noHistoryLabel.setBorder(new EmptyBorder(50, 10, 50, 10));
                historyPanel.add(noHistoryLabel);
                return;
            }
            
            // Get history file
            File historyFile = File_Utils.getUserHistoryFile();
            
            if (!historyFile.exists()) {
                JLabel noHistoryLabel = new JLabel("No workout history found. Generate some workouts first!", 
                    SwingConstants.CENTER);
                noHistoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
                noHistoryLabel.setBorder(new EmptyBorder(50, 10, 50, 10));
                historyPanel.add(noHistoryLabel);
                return;
            }
            
            // Read all history settings from history file
            List<File_Utils.UserPreferences> settingsList = File_Utils.readHistorySettings(historyFile);
            
            if (settingsList.isEmpty()) {
                JLabel noHistoryLabel = new JLabel("No previous workout settings found.", 
                    SwingConstants.CENTER);
                noHistoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
                noHistoryLabel.setBorder(new EmptyBorder(50, 10, 50, 10));
                historyPanel.add(noHistoryLabel);
                return;
            }
            
            // Display each setting (most recent first)
            for (int i = settingsList.size() - 1; i >= 0; i--) {
                File_Utils.UserPreferences settings = settingsList.get(i);
                JPanel settingCard = createSettingCard(settings, settingsList.size() - i);
                historyPanel.add(settingCard);
                historyPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading history: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        historyPanel.revalidate();
        historyPanel.repaint();
    }
    
    private JPanel createSettingCard(File_Utils.UserPreferences settings, int index) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(new Color(240, 248, 255));
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(900, 250));
        
        // Header with setting number
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblSettingNum = new JLabel("History Setting #" + index);
        lblSettingNum.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblSettingNum.setForeground(new Color(25, 25, 112));
        
        JButton btnRestore = new JButton("Restore These Settings");
        btnRestore.setBackground(new Color(34, 139, 34));
        btnRestore.setForeground(Color.WHITE);
        btnRestore.addActionListener(e -> restoreSettings(settings));
        
        headerPanel.add(lblSettingNum, BorderLayout.WEST);
        headerPanel.add(btnRestore, BorderLayout.EAST);
        
        // Details panel - FIXED: Check if sections are empty before adding
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(240, 248, 255));
        
        // Check each section and only add if it has content
        boolean hasContent = false;
        
        // Goal section
        if (!settings.getGoal().isEmpty()) {
            addDetailSection(detailsPanel, "Goal:", settings.getGoal());
            hasContent = true;
        }
        
        // Equipment section
        if (!settings.getEquipment().isEmpty()) {
            addDetailSection(detailsPanel, "Equipment:", settings.getEquipment());
            hasContent = true;
        }
        
        // Home availability section
        if (!settings.getAvailabilityHome().isEmpty()) {
            addDetailSection(detailsPanel, "Home Days:", settings.getAvailabilityHome());
            hasContent = true;
        }
        
        // Gym availability section
        if (!settings.getAvailabilityGym().isEmpty()) {
            addDetailSection(detailsPanel, "Gym Days:", settings.getAvailabilityGym());
            hasContent = true;
        }
        
        // If nothing was added (all sections empty), show a message
        if (!hasContent) {
            JLabel emptyLabel = new JLabel("No settings selected for this entry");
            emptyLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            detailsPanel.add(emptyLabel);
        }
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    // FIXED: Better method to add detail sections
    private void addDetailSection(JPanel panel, String label, List<String> values) {
        if (values == null || values.isEmpty()) {
            return; // Don't add empty sections
        }
        
        JPanel detailRow = new JPanel(new BorderLayout(10, 5));
        detailRow.setBackground(new Color(240, 248, 255));
        
        JLabel detailLabel = new JLabel(label);
        detailLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        detailLabel.setPreferredSize(new Dimension(100, 20));
        
        // Join values with comma
        String valueText = String.join(", ", values);
        JLabel valueLabel = new JLabel(valueText);
        valueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        
        detailRow.add(detailLabel, BorderLayout.WEST);
        detailRow.add(valueLabel, BorderLayout.CENTER);
        detailRow.setBorder(new EmptyBorder(3, 0, 3, 0));
        
        panel.add(detailRow);
    }
    
    private void restoreSettings(File_Utils.UserPreferences settings) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Restore these workout settings?\nThis will replace your current settings.",
            "Confirm Restore",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Save as current preferences
                File_Utils.savePreferences(settings);
                
                JOptionPane.showMessageDialog(this,
                    "Settings restored successfully!\nGenerate new workouts to apply them.",
                    "Restore Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                this.dispose();
                new Settings().setVisible(true);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error restoring settings: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void goBackToHome() {
        this.dispose();
        new HomePage().setVisible(true);
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                HistoryPage frame = new HistoryPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}