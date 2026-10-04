import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class Settings extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private File_Utils.UserPreferences currentPrefs;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Settings frame = new Settings();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Settings() {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        // Load current user preferences
        File userFile = File_Utils.getUserFile();
        currentPrefs = File_Utils.readCurrentPreferences(userFile);

        // Main settings panel with grid layout
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 1, 10, 10));
        
        // Goal Panel
        JPanel goalPanel = createPanel("Set Fitness Goal", new Color(168, 198, 250), 
                                      "G:\\My Drive\\Computer science IA\\Plannify\\src\\clipart1966376.png",
                                      e -> openGoalPage());
        
        // Availability Panel
        JPanel availabilityPanel = createPanel("Set Availability & Location", new Color(136, 179, 249),
                                              "G:\\My Drive\\Computer science IA\\Plannify\\src\\clipart91628.png",
                                              e -> openAvailabilityPage());
        
        // Equipment Panel
        JPanel equipmentPanel = createPanel("Select Equipment", new Color(73, 138, 245),
                                           null,
                                           e -> openEquipmentPage());

        settingsPanel.add(goalPanel);
        settingsPanel.add(availabilityPanel);
        settingsPanel.add(equipmentPanel);

        contentPane.add(settingsPanel, BorderLayout.CENTER);

        // Bottom panel with Generate and Back buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Back button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(240, 240, 240));
        
        JButton btnBack = new JButton("Back to Home");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnBack.addActionListener(e -> goBackToHome());
        leftPanel.add(btnBack);

        // Generate button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(240, 240, 240));
        
        JButton btnGenerate = new JButton("Generate Workouts");
        btnGenerate.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnGenerate.setBackground(new Color(34, 139, 34)); 
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFocusPainted(false);
        btnGenerate.addActionListener(e -> generateWorkouts());
        rightPanel.add(btnGenerate);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createPanel(String title, Color color, String iconPath, java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        
        JButton button = new JButton(title);
        button.setBackground(color);
        button.setFont(new Font("Tahoma", Font.BOLD, 24));
        if (iconPath != null) {
            button.setIcon(new ImageIcon(iconPath));
        }
        button.addActionListener(action);
        panel.add(button, BorderLayout.CENTER);
        
        return panel;
    }

    private void openGoalPage() {
        this.dispose();
        new GoalSelection().setVisible(true);
    }

    private void openAvailabilityPage() {
        this.dispose();
        new AvailabilityLocation().setVisible(true);
    }

    private void openEquipmentPage() {
        this.dispose();
        new EquipmentSelection().setVisible(true);
    }

    private void goBackToHome() {
        this.dispose();
        new HomePage().setVisible(true);
    }

    private void generateWorkouts() {
        System.out.println("\n=== GENERATING WORKOUTS ===");
        
        try {
            // FIRST check if we have valid settings
            if (currentPrefs.getGoal().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "❌ Please select a goal first!",
                    "Missing Goal",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (currentPrefs.getEquipment().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "❌ Please select at least one equipment!",
                    "Missing Equipment",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (currentPrefs.getAvailabilityHome().isEmpty() && 
                currentPrefs.getAvailabilityGym().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "❌ Please select at least one day for Home or Gym!",
                    "Missing Availability",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // FIXED: Don't save preferences here - let the generation process handle it
            // The current settings are already loaded from the main file
            
            // Generate matched workouts
            System.out.println("Matching workouts with user preferences...");
            List<Workout> matchedWorkouts = File_Utils.matchWorkoutsWithUser(currentPrefs);
            
            if (matchedWorkouts.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "❌ NO WORKOUTS MATCHED!\n\n" +
                    "Try selecting different equipment or goals.",
                    "No Workouts Found",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Distribute workouts to days
            System.out.println("Distributing " + matchedWorkouts.size() + " workouts...");
            WorkoutDistributor.distributeWorkouts(currentPrefs, matchedWorkouts);
            
            // FIXED: Only save to history ONCE, when workouts are actually generated
            // This prevents duplicate history entries
            File_Utils.savePreferences(currentPrefs);
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "✅ SUCCESS!\n\n" +
                "Generated " + matchedWorkouts.size() + " personalized workouts!\n" +
                "Settings saved to history.",
                "Workouts Generated",
                JOptionPane.INFORMATION_MESSAGE);
                
            // Return to home page
            this.dispose();
            new HomePage().setVisible(true);
            
        } catch (Exception ex) {
            System.out.println("ERROR generating workouts: " + ex.getMessage());
            ex.printStackTrace();
            
            JOptionPane.showMessageDialog(this,
                "❌ ERROR GENERATING WORKOUTS!\n\n" +
                "Error: " + ex.getMessage(),
                "Generation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}