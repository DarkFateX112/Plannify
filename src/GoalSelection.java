import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Arrays;

public class GoalSelection extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String selectedGoal = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GoalSelection frame = new GoalSelection();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GoalSelection() {
        setTitle("Goal Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1001, 627);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Pre-select current goal if exists
        File userFile = File_Utils.getUserFile();
        File_Utils.UserPreferences currentPrefs = File_Utils.readCurrentPreferences(userFile);
        
        if (!currentPrefs.getGoal().isEmpty()) {
            selectedGoal = currentPrefs.getGoal().get(0);
        }

        // Healthy lifestyle panel
        JPanel panel1 = new JPanel();
        panel1.setBackground(new Color(168, 198, 250));
        panel1.setBounds(0, 0, 249, 588);
        contentPane.add(panel1);
        panel1.setLayout(null);

        JButton btnHealthy = new JButton("Healthy life style");
        btnHealthy.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnHealthy.setBounds(27, 11, 183, 33);
        panel1.add(btnHealthy);
        
        JButton btnReset = new JButton("Reset Goal");
        btnReset.setBounds(27, 554, 172, 23);
        panel1.add(btnReset);
        btnReset.addActionListener(e -> resetGoal());
        btnHealthy.addActionListener(e -> selectGoal("Healthy lifestyle"));

        // Fat loss panel
        JPanel panel2 = new JPanel();
        panel2.setBackground(new Color(136, 179, 249));
        panel2.setBounds(248, 0, 249, 588);
        contentPane.add(panel2);
        panel2.setLayout(null);

        JButton btnFatLoss = new JButton("Fat loss");
        btnFatLoss.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnFatLoss.setBounds(33, 11, 183, 33);
        panel2.add(btnFatLoss);
        btnFatLoss.addActionListener(e -> selectGoal("Fat loss"));

        // Muscle build panel
        JPanel panel3 = new JPanel();
        panel3.setBackground(new Color(73, 138, 245));
        panel3.setBounds(496, 0, 248, 588);
        contentPane.add(panel3);
        panel3.setLayout(null);

        JButton btnMuscle = new JButton("Muscle build");
        btnMuscle.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnMuscle.setBounds(29, 11, 183, 33);
        panel3.add(btnMuscle);
        btnMuscle.addActionListener(e -> selectGoal("Muscle build"));

        // Build athleticism panel
        JPanel panel4 = new JPanel();
        panel4.setBackground(new Color(13, 99, 242));
        panel4.setBounds(737, 0, 248, 588);
        contentPane.add(panel4);
        panel4.setLayout(null);

        JButton btnAthletic = new JButton("Build athleticism");
        btnAthletic.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnAthletic.setBounds(38, 11, 183, 33);
        panel4.add(btnAthletic);
        btnAthletic.addActionListener(e -> selectGoal("Build athleticism"));

        // OK Button
        JButton btnOk = new JButton("Ok");
        btnOk.setBounds(149, 554, 89, 23);
        panel4.add(btnOk);
        btnOk.addActionListener(e -> saveGoalAndReturn());

        // Update button appearances based on current selection
        updateButtonAppearance();
    }

    private void selectGoal(String goal) {
        selectedGoal = goal;
        updateButtonAppearance();
        JOptionPane.showMessageDialog(this, 
            "Goal selected: " + goal + "\nClick OK to confirm.", 
            "Goal Selected", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateButtonAppearance() {
        // This method would visually indicate the selected goal
    }

    private void resetGoal() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to reset your goal?", 
            "Reset Goal", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            selectedGoal = null;
            JOptionPane.showMessageDialog(this, 
                "Goal has been reset.", 
                "Reset Complete", 
                JOptionPane.INFORMATION_MESSAGE);
            updateButtonAppearance();
        }
    }

    private void saveGoalAndReturn() {
        String email = Session.getCurrentUserEmail();
        if (email == null) {
            JOptionPane.showMessageDialog(this, "No user is logged in!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedGoal == null) {
            JOptionPane.showMessageDialog(this, "Please select a goal before clicking OK", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update current preferences
        File_Utils.UserPreferences prefs = new File_Utils.UserPreferences();
        prefs.setGoal(Arrays.asList(selectedGoal));
        
        // Load current equipment and days to keep them
        File userFile = File_Utils.getUserFile();
        File_Utils.UserPreferences currentPrefs = File_Utils.readCurrentPreferences(userFile);
        prefs.setEquipment(currentPrefs.getEquipment());
        prefs.setAvailabilityHome(currentPrefs.getAvailabilityHome());
        prefs.setAvailabilityGym(currentPrefs.getAvailabilityGym());

        // Save preferences
        File_Utils.savePreferences(prefs);

        JOptionPane.showMessageDialog(this, 
            "Goal \"" + selectedGoal + "\" saved!\n\nClick 'Generate' on the Settings page to create your workouts.", 
            "Success", JOptionPane.INFORMATION_MESSAGE);

        // Redirect to settings page
        this.dispose();
        new Settings().setVisible(true);
    }
}