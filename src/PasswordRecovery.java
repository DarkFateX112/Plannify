import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PasswordRecovery extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtEmail;
    private JPasswordField newPasswordField;
    private JPasswordField reNewPasswordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PasswordRecovery frame = new PasswordRecovery();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PasswordRecovery() {
        setTitle("Password Recovery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);

        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        // Title
        JLabel lblTitle = new JLabel("Reset Your Password", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 28));
        lblTitle.setForeground(new Color(13, 99, 242));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        formPanel.add(lblEmail, gbc);

        gbc.gridy++;
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(txtEmail, gbc);

        // New Password
        gbc.gridy++;
        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        formPanel.add(lblNewPassword, gbc);

        gbc.gridy++;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(newPasswordField, gbc);

        // Confirm Password
        gbc.gridy++;
        JLabel lblRePassword = new JLabel("Confirm Password:");
        lblRePassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        formPanel.add(lblRePassword, gbc);

        gbc.gridy++;
        reNewPasswordField = new JPasswordField(20);
        reNewPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        formPanel.add(reNewPasswordField, gbc);

        // Buttons
        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnReset = new JButton("Reset Password");
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReset.setBackground(new Color(34, 139, 34));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        btnReset.setPreferredSize(new Dimension(180, 40));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnCancel.setPreferredSize(new Dimension(120, 40));
        
        buttonPanel.add(btnReset);
        buttonPanel.add(btnCancel);
        
        gbc.gridy++;
        formPanel.add(buttonPanel, gbc);

        contentPane.add(formPanel, BorderLayout.CENTER);

        // Info label
        JLabel lblInfo = new JLabel(
            "<html><div style='text-align: center;'>" +
            "Enter your email and new password.<br>" +
            "Your password will be updated immediately." +
            "</div></html>", 
            SwingConstants.CENTER);
        lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblInfo.setForeground(Color.DARK_GRAY);
        lblInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        contentPane.add(lblInfo, BorderLayout.SOUTH);

        // Action Listeners
        btnReset.addActionListener(e -> resetPassword());
        btnCancel.addActionListener(e -> {
            dispose();
            new MainPage().setVisible(true);
        });
    }

    private boolean userExists(String email) {
        // Hardcoded path
        File usersFile = new File("G:\\My Drive\\Computer science IA\\Plannify\\users.txt");
        if (!usersFile.exists()) {
            return false;
        }

        try (Scanner scanner = new Scanner(usersFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 3);
                if (parts.length > 0 && parts[0].equals(email)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void resetPassword() {
        String email = txtEmail.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());
        String rePassword = new String(reNewPasswordField.getPassword());

        // Validate inputs
        if (email.isEmpty() || newPassword.isEmpty() || rePassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "❌ Please fill in all fields",
                "Missing Information",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                "❌ Invalid email format!\n\n" +
                "Please enter a valid email address:\n" +
                "• Must contain @ symbol\n" +
                "• Must have a domain (e.g., gmail.com)\n" +
                "• Example: user@example.com",
                "Invalid Email",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check password length
        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "❌ Password too short!\n\n" +
                "Password must be at least 6 characters long.",
                "Weak Password",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email exists
        if (!userExists(email)) {
            JOptionPane.showMessageDialog(this,
                "❌ Email not found!\n\n" +
                "This email is not registered in our system.\n" +
                "Please check the email or create a new account.",
                "Email Not Found",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if passwords match
        if (!newPassword.equals(rePassword)) {
            JOptionPane.showMessageDialog(this,
                "❌ Passwords do not match!\n\n" +
                "Please make sure both passwords are identical.",
                "Password Mismatch",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm with user
        int confirm = JOptionPane.showConfirmDialog(this,
            "Reset password for: " + email + "?\n\n" +
            "⚠️ This will change your password immediately.\n" +
            "You will need to use the new password to login.",
            "Confirm Password Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return; // User canceled
        }

        // Update password directly
        if (updatePasswordInFile(email, newPassword)) {
            JOptionPane.showMessageDialog(this,
                "✅ Password reset successfully!\n\n" +
                "Your password has been updated.\n" +
                "You can now login with your new password.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            txtEmail.setText("");
            newPasswordField.setText("");
            reNewPasswordField.setText("");
            
            // Go to login page after a delay
            Timer timer = new Timer(2000, e -> {
                dispose();
                new MainPage().setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
            
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Failed to reset password!\n\n" +
                "There was an error updating your password.\n" +
                "Please try again or contact support.",
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
                String[] parts = line.split(",", 3);
                if (parts.length >= 2 && parts[0].equals(email)) {
                    // Keep the name if it exists (parts[2])
                    String updatedLine = parts[0] + "," + newPassword;
                    if (parts.length == 3) {
                        updatedLine += "," + parts[2];
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
}