import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class EquipmentSelection extends JFrame { 
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private List<JCheckBox> checkBoxes;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EquipmentSelection frame = new EquipmentSelection();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EquipmentSelection() {
        checkBoxes = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Equipment Selection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(168, 198, 250));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // Title
        JLabel lblTitle = new JLabel("Select Your Equipment", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Impact", Font.PLAIN, 36));
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        // Equipment checkboxes
        String[] equipmentList = {
            "Dumbbell", "Skipping rope", "Resistance Band", "Kettlebell",
            "Treadmill", "Bench Press", "Pull-up Bar", "Medicine Ball",
            "Rowing Machine", "Exercise Bike", "Yoga Mat",
            "Weighted Vest", "Ab Roller", "Battle Rope", "Foam Roller",
            "Punching Bag", "Stepper", "Power Rack", "Glute Bands", "None"
        };

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(0, 2, 10, 10));
        checkBoxPanel.setBackground(new Color(168, 198, 250));

        for (String item : equipmentList) {
            JCheckBox cb = new JCheckBox(item);
            cb.setFont(new Font("Tahoma", Font.PLAIN, 16));
            checkBoxPanel.add(cb);
            checkBoxes.add(cb);
        }

        JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(168, 198, 250));
        
        // Reset button
        JButton btnReset = new JButton("Reset Equipment");
        btnReset.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnReset.addActionListener(e -> resetEquipment());
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnBack.addActionListener(e -> goBackToSettings());
        
        // Save button
        JButton btnOk = new JButton("Save Equipment");
        btnOk.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnOk.addActionListener(e -> saveEquipmentAndReturn());
        
        buttonPanel.add(btnReset);
        buttonPanel.add(btnBack);
        buttonPanel.add(btnOk);
        
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void resetEquipment() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to reset all equipment selections?", 
            "Reset Equipment", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            for (JCheckBox cb : checkBoxes) {
                cb.setSelected(false);
            }
            JOptionPane.showMessageDialog(this, 
                "Equipment selections have been reset.", 
                "Reset Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveEquipmentAndReturn() {
        String email = Session.getCurrentUserEmail();
        if (email == null) {
            JOptionPane.showMessageDialog(this, "No user is logged in!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save selected equipment
        List<String> selectedEquipment = new ArrayList<>();
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                selectedEquipment.add(cb.getText());
            }
        }
        
        // Update current preferences
        File_Utils.UserPreferences prefs = new File_Utils.UserPreferences();
        prefs.setEquipment(selectedEquipment);
        
        // Load current goal and days to keep them
        File userFile = File_Utils.getUserFile();
        File_Utils.UserPreferences currentPrefs = File_Utils.readCurrentPreferences(userFile);
        prefs.setGoal(currentPrefs.getGoal());
        prefs.setAvailabilityHome(currentPrefs.getAvailabilityHome());
        prefs.setAvailabilityGym(currentPrefs.getAvailabilityGym());

        // Save preferences
        File_Utils.savePreferences(prefs);

        JOptionPane.showMessageDialog(this, 
            "Equipment preferences saved successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);

        goBackToSettings();
    }

    private void goBackToSettings() {
        this.dispose();
        new Settings().setVisible(true);
    }
}