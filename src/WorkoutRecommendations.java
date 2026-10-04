import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class WorkoutRecommendations extends JFrame {
    private JPanel contentPane;
    private JPanel workoutPanel;
    private JComboBox<String> dateComboBox;
    private String selectedDay;
    private String selectedDate;
    
    // Store all workouts for the current day
    private List<String> currentDayWorkouts = new ArrayList<>();

    public WorkoutRecommendations() {
        setTitle("Daily Workouts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        // Title
        JLabel lblTitle = new JLabel("Your Workouts", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Impact", Font.PLAIN, 36));
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        // Date selection
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        datePanel.add(new JLabel("Select Date: "));
        dateComboBox = new JComboBox<>(getDateOptions());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd (EEEE)");
        String today = sdf.format(new Date());
        dateComboBox.setSelectedItem(today);
        
        dateComboBox.addActionListener(e -> loadWorkoutsForSelectedDate());
        datePanel.add(dateComboBox);
        contentPane.add(datePanel, BorderLayout.NORTH);

        // Workout display panel
        workoutPanel = new JPanel();
        workoutPanel.setLayout(new BoxLayout(workoutPanel, BoxLayout.Y_AXIS));
        workoutPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(workoutPanel);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with Finish All button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Left side: Back button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(240, 240, 240));
        
        JButton btnBack = new JButton("Back to Home");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnBack.addActionListener(e -> {
            this.dispose();
            new HomePage().setVisible(true);
        });
        leftPanel.add(btnBack);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnRefresh.addActionListener(e -> loadWorkoutsForSelectedDate());
        leftPanel.add(btnRefresh);
        
        // Right side: Finish All button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(240, 240, 240));
        
        JButton btnFinishAll = new JButton("Finish All Workouts");
        btnFinishAll.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnFinishAll.setBackground(new Color(34, 139, 34)); // Green
        btnFinishAll.setForeground(Color.WHITE);
        btnFinishAll.setFocusPainted(false);
        btnFinishAll.setPreferredSize(new Dimension(250, 50));
        btnFinishAll.addActionListener(e -> finishAllWorkouts());
        
        rightPanel.add(btnFinishAll);
        
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);
        
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        // Load today's workouts
        loadWorkoutsForSelectedDate();
    }

    private String[] getDateOptions() {
        String[] dates = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd (EEEE)");
        Calendar cal = Calendar.getInstance();
        
        for (int i = 0; i < 7; i++) {
            dates[i] = sdf.format(cal.getTime());
            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }

    private void loadWorkoutsForSelectedDate() {
        workoutPanel.removeAll();
        currentDayWorkouts.clear();
        
        try {
            String selectedDateWithDay = (String) dateComboBox.getSelectedItem();
            if (selectedDateWithDay == null) return;
            
            selectedDate = selectedDateWithDay.split(" ")[0];
            selectedDay = selectedDateWithDay.split("\\(")[1].replace(")", "").trim();
            
            // Get all workouts for the selected day
            List<String> workouts = WorkoutDistributor.getWorkoutsForDay(selectedDay);
            currentDayWorkouts.addAll(workouts);
            
            if (workouts.isEmpty()) {
                JLabel noWorkoutsLabel = new JLabel(
                    "<html><center><b>No workouts scheduled for " + selectedDay + "</b><br><br>" +
                    "Go to Settings → Generate Workouts to create your workout schedule</center></html>", 
                    SwingConstants.CENTER);
                noWorkoutsLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
                noWorkoutsLabel.setBorder(new EmptyBorder(50, 10, 50, 10));
                workoutPanel.add(noWorkoutsLabel);
            } else {
                // Show day header
                JLabel dayLabel = new JLabel(selectedDay + " - " + selectedDate, SwingConstants.CENTER);
                dayLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
                dayLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
                workoutPanel.add(dayLabel);
                
                // Display all workouts
                for (String workoutStr : workouts) {
                    JPanel workoutCard = createWorkoutCard(workoutStr);
                    workoutPanel.add(workoutCard);
                    workoutPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
            
        } catch (Exception ex) {
            JLabel errorLabel = new JLabel(
                "<html><center><b>Error loading workouts</b><br>" +
                "Make sure you have generated workouts first</center></html>", 
                SwingConstants.CENTER);
            errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            errorLabel.setForeground(Color.RED);
            errorLabel.setBorder(new EmptyBorder(50, 10, 50, 10));
            workoutPanel.add(errorLabel);
        }
        
        workoutPanel.revalidate();
        workoutPanel.repaint();
    }
    
    private String extractWorkoutName(String workoutStr) {
        String[] parts = workoutStr.split("\\|");
        return parts[0].trim();
    }

    private JPanel createWorkoutCard(String workoutStr) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(900, 180));
        
        String[] parts = workoutStr.split("\\|");
        String workoutName = parts[0].trim();
        
        JLabel nameLabel = new JLabel(workoutName);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        nameLabel.setForeground(new Color(13, 99, 242)); // Blue color
        
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(0, 2, 10, 5));
        detailsPanel.setBackground(Color.WHITE);
        
        for (int i = 1; i < parts.length; i++) {
            String[] detail = parts[i].trim().split(":", 2);
            if (detail.length == 2) {
                JLabel detailLabel = new JLabel(detail[0].trim() + ":");
                detailLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
                detailLabel.setForeground(Color.DARK_GRAY);
                detailsPanel.add(detailLabel);
                
                JLabel valueLabel = new JLabel(detail[1].trim());
                valueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
                valueLabel.setForeground(Color.BLACK);
                detailsPanel.add(valueLabel);
            }
        }
        
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void finishAllWorkouts() {
        if (currentDayWorkouts.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No workouts to finish for " + selectedDay + ".",
                "No Workouts",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Ask for confirmation
        int confirm = JOptionPane.showConfirmDialog(this,
            "Finish all " + currentDayWorkouts.size() + " workouts for " + selectedDay + "?\n\n" +
            "This will mark all workouts as completed and remove them from view.",
            "Confirm Finish All",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Save all workouts to finished_workouts.txt
        String userEmail = Session.getCurrentUserEmail();
        if (userEmail == null) {
            JOptionPane.showMessageDialog(this,
                "You must be logged in to save workouts.",
                "Login Required",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            File finishedFile = new File("G:\\My Drive\\Computer science IA\\Plannify\\finished_workouts.txt");
            try (FileWriter fw = new FileWriter(finishedFile, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                
                int totalCalories = 0;
                int savedCount = 0;
                
                for (String workoutStr : currentDayWorkouts) {
                    String workoutName = extractWorkoutName(workoutStr);
                    String calories = extractCalories(workoutStr);
                    
                    bw.write(selectedDate + "," + workoutName + "," + calories + "," + userEmail);
                    bw.newLine();
                    savedCount++;
                    
                    try {
                        totalCalories += Integer.parseInt(calories);
                    } catch (NumberFormatException e) {
                        // Ignore if calories not parseable
                    }
                }
                
                // Show success message
                JOptionPane.showMessageDialog(this,
                    "✅ " + savedCount + " workouts completed!\n\n" +
                    "Total calories burned: " + totalCalories + " kcal\n" +
                    "All workouts have been removed from view.",
                    "Workouts Finished",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Remove all workouts from display
                workoutPanel.removeAll();
                
                // Show completion message
                JLabel completedLabel = new JLabel(
                    "<html><center><b>All workouts completed for " + selectedDay + "!</b><br><br>" +
                    "✅ Great job! You've finished all your workouts.<br>" +
                    "Total calories burned: " + totalCalories + " kcal" +
                    "</center></html>", 
                    SwingConstants.CENTER);
                completedLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
                completedLabel.setForeground(new Color(34, 139, 34)); // Green color for success
                completedLabel.setBorder(new EmptyBorder(50, 10, 50, 10));
                workoutPanel.add(completedLabel);
                
                workoutPanel.revalidate();
                workoutPanel.repaint();
                
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving workouts: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String extractCalories(String workoutStr) {
        String[] parts = workoutStr.split("\\|");
        for (String part : parts) {
            if (part.contains("Calories:")) {
                String calorieStr = part.replace("Calories:", "").trim().split(" ")[0];
                try {
                    // Validate it's a number
                    Integer.parseInt(calorieStr);
                    return calorieStr;
                } catch (NumberFormatException e) {
                    return "0";
                }
            }
        }
        return "0";
    }
}