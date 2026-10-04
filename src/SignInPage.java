import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SignInPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLayeredPane contentPane;
    private JTextField txtEmail;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;

    // FIXED: Use hardcoded paths
    private final File appFolder = new File("G:\\My Drive\\Computer science IA\\Plannify\\user_files");
    private final File usersFile = new File("G:\\My Drive\\Computer science IA\\Plannify\\users.txt");

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SignInPage frame = new SignInPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SignInPage() {
        setTitle("Sign in page");
        setBackground(Color.GRAY);
        setForeground(Color.YELLOW);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 625);

        contentPane = new JLayeredPane();
        contentPane.setBackground(new Color(240, 240, 240));
        contentPane.setForeground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(168, 198, 250));
        panel.setBounds(493, 0, 491, 586);
        contentPane.add(panel);
        panel.setLayout(null);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Verdana", Font.PLAIN, 11));
        txtEmail.setBounds(5, 70, 187, 27);
        panel.add(txtEmail);
        txtEmail.setColumns(10);

        JTextArea txtrLogin = new JTextArea();
        txtrLogin.setEditable(false);
        txtrLogin.setBounds(5, 0, 419, 44);
        txtrLogin.setBackground(new Color(168, 198, 250));
        txtrLogin.setFont(new Font("Monospaced", Font.BOLD, 30));
        txtrLogin.setText("Sign into an account");
        panel.add(txtrLogin);

        passwordField = new JPasswordField();
        passwordField.setBounds(5, 118, 187, 27);
        panel.add(passwordField);

        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(5, 169, 187, 27);
        panel.add(passwordField_1);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(5, 58, 46, 14);
        panel.add(lblEmail);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(5, 104, 68, 14);
        panel.add(lblPassword);

        JLabel lblRePassword = new JLabel("Re-enter password");
        lblRePassword.setBounds(5, 156, 120, 14);
        panel.add(lblRePassword);

        JButton btnEnter = new JButton("Enter");
        btnEnter.setBounds(5, 207, 89, 23);
        panel.add(btnEnter);

        btnEnter.addActionListener(e -> {
            registerUser();
        });

        JButton btnLogin = new JButton("Already have an account? Login in here");
        btnLogin.setBackground(new Color(168, 198, 250));
        btnLogin.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogin.setBounds(5, 241, 259, 23);
        panel.add(btnLogin);

        btnLogin.addActionListener(e -> {
            this.setVisible(false);
            new MainPage().setVisible(true);
        });

        JTextArea quoteArea = new JTextArea();
        quoteArea.setEditable(false);
        quoteArea.setBackground(new Color(168, 198, 250));
        quoteArea.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        quoteArea.setText("\"The journey of a thousand \nmiles begins with one step\"\n- Lao Tzu");
        quoteArea.setBounds(151, 346, 236, 106);
        panel.add(quoteArea);

        JLabel lblBackground = new JLabel(new ImageIcon("G:\\My Drive\\Computer science IA\\Plannify\\src\\e7133d8303bd92e7bea4e76c43d36bc1.jpg"));
        lblBackground.setBounds(0, 0, 495, 586);
        contentPane.add(lblBackground);
    }

    private void registerUser() {
        String email = txtEmail.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String rePassword = new String(passwordField_1.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ADD THIS: Validate email format
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
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters long",
                "Weak Password",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(rePassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match!\n\n" +
                "Please make sure both passwords are identical.",
                "Password Mismatch", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // CHECK IF USER EXISTS
        if (userExists(email)) {
            JOptionPane.showMessageDialog(this, 
                "❌ Email already registered!\n\n" +
                "This email is already in use. Please use a different email or login.",
                "Email Exists", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create folders/files
            if (!appFolder.exists()) {
                System.out.println("DEBUG: Creating folder: " + appFolder.getAbsolutePath());
                appFolder.mkdirs();
            }
            if (!usersFile.exists()) {
                System.out.println("DEBUG: Creating file: " + usersFile.getAbsolutePath());
                usersFile.createNewFile();
            }

            // WRITE TO USERS.TXT
            try (FileWriter fw = new FileWriter(usersFile, true)) {
                fw.write(email + "," + password + System.lineSeparator());
                System.out.println("DEBUG: Added user to users.txt: " + email);
            }

            // Create user's personal file
            String safeFileName = email.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
            File userFile = new File(appFolder, safeFileName);
            userFile.createNewFile();
            System.out.println("DEBUG: Created user file: " + userFile.getAbsolutePath());

            JOptionPane.showMessageDialog(this, 
                "✅ Account created successfully!\n\n" +
                "Welcome to Plannify! You can now login with your credentials.",
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Go to login page
            this.setVisible(false);
            new MainPage().setVisible(true);

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "❌ Error saving user data!\n\n" +
                "Please try again or contact support.\n" +
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // ADD THIS METHOD: Email validation
    private boolean isValidEmail(String email) {
        // Better email validation regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private boolean userExists(String email) {
        if (!usersFile.exists()) {
            return false;
        }

        try (Scanner sc = new Scanner(usersFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(email)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}