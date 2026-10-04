import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class AvailabilityLocation extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> comboBox;
    private Map<String, JCheckBox> dayCheckBoxes = new LinkedHashMap<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AvailabilityLocation frame = new AvailabilityLocation();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AvailabilityLocation() {
        setTitle("Availability & Location");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1001, 727);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(168, 198, 250));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        String[] places = { "Home", "Gym" };
        comboBox = new JComboBox<>(places);
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 30));
        comboBox.setBounds(30, 27, 231, 43);
        contentPane.add(comboBox);

        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        int yPos = 134;
        for (String day : days) {
            JLabel lbl = new JLabel(day);
            lbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
            lbl.setBounds(30, yPos, 200, 40);
            contentPane.add(lbl);

            JCheckBox cb = new JCheckBox();
            cb.setBackground(new Color(168, 198, 250));
            cb.setBounds(250, yPos + 10, 21, 21);
            contentPane.add(cb);

            dayCheckBoxes.put(day, cb);
            yPos += 75;
        }

        // ADD RESET BUTTON
        JButton btnReset = new JButton("Reset Days");
        btnReset.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnReset.setBounds(30, 644, 150, 33);
        contentPane.add(btnReset);
        btnReset.addActionListener(e -> resetAvailability());

        JButton btnOk = new JButton("Ok");
        btnOk.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnOk.setBounds(845, 644, 130, 33);
        contentPane.add(btnOk);
        btnOk.addActionListener(e -> saveAvailability());
    }

    private void resetAvailability() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to reset all selected days?", 
            "Reset Availability", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            for (JCheckBox cb : dayCheckBoxes.values()) {
                cb.setSelected(false);
            }
            JOptionPane.showMessageDialog(this, 
                "All days have been reset.", 
                "Reset Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveAvailability() {
        String email = Session.getCurrentUserEmail();
        if (email == null) {
            JOptionPane.showMessageDialog(this, "No user logged in!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File userFile = File_Utils.getUserFile();
        File_Utils.UserPreferences currentPrefs = File_Utils.readCurrentPreferences(userFile);

        String location = comboBox.getSelectedItem().toString();
        
        List<String> selectedDays = new ArrayList<>();
        for (Map.Entry<String, JCheckBox> entry : dayCheckBoxes.entrySet()) {
            if (entry.getValue().isSelected()) selectedDays.add(entry.getKey());
        }

        // Update the correct list based on location
        if ("Home".equals(location)) {
            currentPrefs.setAvailabilityHome(selectedDays);
        } else if ("Gym".equals(location)) {
            currentPrefs.setAvailabilityGym(selectedDays);
        }

        // Save preferences (will save current preferences and append to history)
        File_Utils.savePreferences(currentPrefs);

        JOptionPane.showMessageDialog(this, location + " availability saved!");

        // Redirect back to settings page
        this.setVisible(false);
        new Settings().setVisible(true);
    }
}