import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HomePage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HomePage frame = new HomePage();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public HomePage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1001, 627);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Top panel - Workout
        JPanel workoutPanel = new JPanel();
        workoutPanel.setBackground(new Color(168, 198, 250));
        workoutPanel.setBorder(null);
        workoutPanel.setBounds(0, 0, 985, 147);
        contentPane.add(workoutPanel);
        workoutPanel.setLayout(null);

        // Streak display
        String streakCount = loadStreakCount();
        JLabel streakLabel = new JLabel(streakCount);
        streakLabel.setBounds(870, 30, 100, 80);
        streakLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        streakLabel.setForeground(Color.RED);
        streakLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        streakLabel.setVerticalTextPosition(SwingConstants.CENTER);
        streakLabel.setIcon(new ImageIcon("G:\\My Drive\\Computer science IA\\Plannify\\src\\clipart258609 (1).png"));
        workoutPanel.add(streakLabel);

        JButton btnWorkout = new JButton("Workout");
        btnWorkout.setFont(new Font("Tahoma", Font.PLAIN, 30));
        btnWorkout.setBounds(10, 53, 206, 52);
        workoutPanel.add(btnWorkout);
        btnWorkout.addActionListener(e -> {
            this.setVisible(false);
            new WorkoutRecommendations().setVisible(true);
        });

        // Second panel - Settings
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(new Color(136, 179, 249));
        settingsPanel.setBorder(null);
        settingsPanel.setBounds(0, 147, 985, 147);
        contentPane.add(settingsPanel);
        settingsPanel.setLayout(null);

        JLabel settingsIcon = new JLabel("");
        settingsIcon.setIcon(new ImageIcon("G:\\My Drive\\Computer science IA\\Plannify\\src\\clipart61893 (1).png"));
        settingsIcon.setBounds(787, -39, 188, 245);
        settingsPanel.add(settingsIcon);

        JButton btnSettings = new JButton("Settings");
        btnSettings.setFont(new Font("Tahoma", Font.PLAIN, 30));
        btnSettings.setBounds(10, 52, 206, 52);
        settingsPanel.add(btnSettings);
        btnSettings.addActionListener(e -> {
            this.setVisible(false);
            new Settings().setVisible(true);
        });

        // Third panel - Calorie Checker
        JPanel caloriePanel = new JPanel();
        caloriePanel.setBackground(new Color(73, 138, 245));
        caloriePanel.setBorder(null);
        caloriePanel.setBounds(0, 294, 985, 140);
        contentPane.add(caloriePanel);
        caloriePanel.setLayout(null);

        JLabel calorieIcon = new JLabel("");
        calorieIcon.setIcon(new ImageIcon("G:\\My Drive\\Computer science IA\\Plannify\\src\\clipart377759.png"));
        calorieIcon.setBounds(757, -32, 196, 259);
        caloriePanel.add(calorieIcon);

        JButton btnCalorieChecker = new JButton("Calorie Checker");
        btnCalorieChecker.setFont(new Font("Tahoma", Font.PLAIN, 30));
        btnCalorieChecker.setBounds(10, 45, 290, 52);
        caloriePanel.add(btnCalorieChecker);
        btnCalorieChecker.addActionListener(e -> {
            this.setVisible(false);
            new CalorieChecker().setVisible(true);
        });

        // Fourth panel - History
        JPanel historyPanel = new JPanel();
        historyPanel.setBackground(new Color(13, 99, 242));
        historyPanel.setBorder(null);
        historyPanel.setBounds(0, 430, 985, 147);
        contentPane.add(historyPanel);
        historyPanel.setLayout(null);

        JLabel historyIcon = new JLabel("");
        historyIcon.setIcon(new ImageIcon("G:\\My Drive\\Computer science IA\\Plannify\\src\\clipart18867.png"));
        historyIcon.setBounds(789, -37, 170, 259);
        historyPanel.add(historyIcon);

        JButton btnHistory = new JButton("History");
        btnHistory.setFont(new Font("Tahoma", Font.PLAIN, 30));
        btnHistory.setBounds(10, 50, 206, 52);
        historyPanel.add(btnHistory);
        btnHistory.addActionListener(e -> {
            this.setVisible(false);
            new HistoryPage().setVisible(true);
        });
    }

    private String loadStreakCount() {
        try {
            File streakFile = new File("G:\\My Drive\\Computer science IA\\Plannify\\streak.txt");
            if (streakFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(streakFile))) {
                    String line = reader.readLine();
                    if (line != null && !line.trim().isEmpty()) {
                        String streakValue = line.trim();
                        try {
                            int streak = Integer.parseInt(streakValue);
                            if (streak == 0) {
                                return "0";
                            }
                            return streakValue;
                        } catch (NumberFormatException e) {
                            return "0";
                        }
                    }
                }
            }
            return "0";
        } catch (IOException e) {
            return "0";
        }
    }
}