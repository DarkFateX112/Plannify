import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class PasswordResetLinkPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private String userEmail;

    public PasswordResetLinkPage(String email) {
        this.userEmail = email;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Reset Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        // Title
        JLabel lblTitle = new JLabel("Reset Your Password", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 24));
        lblTitle.setBorder(new EmptyBorder(20, 0, 30, 0));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 10, 20));
        formPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        // New Password
        JPanel newPassPanel = new JPanel(new BorderLayout(5, 5));
        JLabel lblNewPass = new JLabel("New Password:");
        lblNewPass.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordField = new JPasswordField(20);
        newPassPanel.add(lblNewPass, BorderLayout.NORTH);
        newPassPanel.add(passwordField, BorderLayout.CENTER);

        // Confirm Password
        JPanel confirmPassPanel = new JPanel(new BorderLayout(5, 5));
        JLabel lblConfirmPass = new JLabel("Confirm Password:");
        lblConfirmPass.setFont(new Font("Tahoma", Font.PLAIN, 14));
        confirmPasswordField = new JPasswordField(20);
        confirmPassPanel.add(lblConfirmPass, BorderLayout.NORTH);
        confirmPassPanel.add(confirmPasswordField, BorderLayout.CENTER);

        formPanel.add(newPassPanel);
        formPanel.add(confirmPassPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnReset = new JButton("Reset Password");
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReset.setBackground(new Color(34, 139, 34));
        btnReset.setForeground(Color.WHITE);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        buttonPanel.add(btnReset);
        buttonPanel.add(btnCancel);

        formPanel.add(new JPanel()); // Spacer
        formPanel.add(buttonPanel);

        contentPane.add(formPanel, BorderLayout.CENTER);

        // Action Listeners
        btnReset.addActionListener(e -> resetPassword());
        btnCancel.addActionListener(e -> {
            dispose();
            new MainPage().setVisible(true);
        });
    }

    private void resetPassword() {
        String newPassword = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in both password fields", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update password in users.txt
        if (updatePasswordInFile(userEmail, newPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Password reset successfully!\nYou can now login with your new password.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MainPage().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to reset password. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean updatePasswordInFile(String email, String newPassword) {
        try {
            String usersFilePath = "G:\\My Drive\\Computer science IA\\Plannify\\users.txt";
            java.nio.file.Path usersPath = java.nio.file.Paths.get(usersFilePath);
            java.util.List<String> lines = java.nio.file.Files.readAllLines(usersPath);
            
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",", 3); // Split into max 3 parts
                if (parts.length >= 2 && parts[0].equals(email)) {
                    // Keep the name if it exists (parts[2])
                    String updatedLine = parts[0] + "," + newPassword;
                    if (parts.length == 3) {
                        updatedLine += "," + parts[2]; // Add the name back
                    }
                    lines.set(i, updatedLine);
                    found = true;
                    break;
                }
            }
            
            if (found) {
                java.nio.file.Files.write(usersPath, lines);
                System.out.println("DEBUG: Password updated for: " + email);
                return true;
            }
        } catch (Exception e) {
            System.out.println("ERROR updating password: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                PasswordResetLinkPage frame = new PasswordResetLinkPage("test@example.com");
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}