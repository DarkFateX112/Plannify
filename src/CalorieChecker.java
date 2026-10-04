import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
//These are the CORRECT imports you need:
import java.util.List;        // This is crucial!
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Date;

public class CalorieChecker extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable calorieTable;
    private DefaultTableModel tableModel;
    
    // Simple data holder class
    private static class WorkoutRecord {
        public String name;
        public int calories;
        
        public WorkoutRecord(String name, int calories) {
            this.name = name;
            this.calories = calories;
        }
    }
    
    public CalorieChecker() {
        setTitle("Calorie Checker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));
        
        // Title
        JLabel lblTitle = new JLabel("Calorie Tracker", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Impact", Font.PLAIN, 32));
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        contentPane.add(lblTitle, BorderLayout.NORTH);
        
        // Table for calories
        String[] columns = {"Date", "Day", "Workouts Completed", "Calories Burned", "Total Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        calorieTable = new JTable(tableModel);
        calorieTable.setRowHeight(30);
        calorieTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
        calorieTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(calorieTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Total calories label
        JLabel lblTotal = new JLabel("Total Calories Burned: 0", SwingConstants.LEFT);
        lblTotal.setFont(new Font("Tahoma", Font.BOLD, 16));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Data");
        JButton btnBack = new JButton("Back to Home");
        
        btnRefresh.addActionListener(e -> loadCalorieData());
        btnBack.addActionListener(e -> goBackToHome());
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        
        bottomPanel.add(lblTotal, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        
        // Load data
        loadCalorieData();
    }
    
    private void loadCalorieData() {
        tableModel.setRowCount(0);
        int totalCalories = 0;
        
        try {
            // Hardcoded path
            File finishedFile = new File("G:\\My Drive\\Computer science IA\\Plannify\\finished_workouts.txt");
            if (!finishedFile.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "No workout data found. Complete some workouts first!", 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Create map WITHOUT diamond operator
            Map<String, List<WorkoutRecord>> workoutsByDate = new TreeMap<String, List<WorkoutRecord>>(Collections.reverseOrder());
            
            try (BufferedReader reader = new BufferedReader(new FileReader(finishedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String date = parts[0];
                        String workoutName = parts[1];
                        int calories = 0;
                        try {
                            calories = Integer.parseInt(parts[2]);
                        } catch (NumberFormatException e) {
                            calories = 0;
                        }
                        String userEmail = parts[3];
                        
                        // Only show current user's data
                        if (userEmail.equals(Session.getCurrentUserEmail())) {
                            // Get or create the list for this date
                            List<WorkoutRecord> records = workoutsByDate.get(date);
                            if (records == null) {
                                records = new ArrayList<WorkoutRecord>();
                                workoutsByDate.put(date, records);
                            }
                            records.add(new WorkoutRecord(workoutName, calories));
                        }
                    }
                }
            }
            
            // Populate table
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            
            // Iterate through map entries
            for (Map.Entry<String, List<WorkoutRecord>> entry : workoutsByDate.entrySet()) {
                String date = entry.getKey();
                List<WorkoutRecord> workouts = entry.getValue();
                
                // Calculate day calories
                int dayCalories = 0;
                for (WorkoutRecord record : workouts) {
                    dayCalories += record.calories;
                }
                totalCalories += dayCalories;
                
                // Parse date to get day name
                Date dateObj = dateFormat.parse(date);
                String dayName = dayFormat.format(dateObj);
                
                // Count unique workouts
                Set<String> uniqueNames = new HashSet<String>();
                for (WorkoutRecord record : workouts) {
                    uniqueNames.add(record.name);
                }
                int uniqueWorkouts = uniqueNames.size();
                
                // Estimate time (assuming 30 minutes per workout)
                int estimatedMinutes = workouts.size() * 30;
                String time = estimatedMinutes + " min";
                
                tableModel.addRow(new Object[]{
                    date, 
                    dayName, 
                    uniqueWorkouts + " workouts", 
                    dayCalories + " kcal", 
                    time
                });
            }
            
            // Update total label
            JLabel lblTotal = (JLabel) ((JPanel) contentPane.getComponent(2)).getComponent(0);
            lblTotal.setText("Total Calories Burned: " + totalCalories + " kcal");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading calorie data: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void goBackToHome() {
        this.dispose();
        new HomePage().setVisible(true);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CalorieChecker frame = new CalorieChecker();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}