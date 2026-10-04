import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.io.*;  
import java.util.Scanner;

public class MainPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLayeredPane contentPane;
    private JTextField txtEmail;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainPage frame = new MainPage();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private boolean validateLogin(String email, String password) {
        // FIXED: Use hardcoded path
        File usersFile = new File("G:\\My Drive\\Computer science IA\\Plannify\\users.txt");
        
        if (!usersFile.exists()) {
            System.out.println("DEBUG: users.txt not found at: " + usersFile.getAbsolutePath());
            return false;
        }

        try (Scanner sc = new Scanner(usersFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 2 && 
                    parts[0].equals(email) && 
                    parts[1].equals(password)) {
                    System.out.println("DEBUG: Login successful for: " + email);
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("DEBUG: Login failed for: " + email);
        return false;
    }

    public MainPage() {
        setTitle("Login page");
        setBackground(Color.GRAY);
        setForeground(Color.YELLOW);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1001, 627);
        contentPane = new JLayeredPane();
        contentPane.setBackground(new Color(240, 240, 240));
        contentPane.setForeground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane); 
        contentPane.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setBackground(new Color(168, 198, 250));
        panel.setBounds(493, 0, 491, 586);
        contentPane.add(panel);
        panel.setLayout(null);
        
        txtEmail = new JTextField();
        txtEmail.setToolTipText("");
        txtEmail.setFont(new Font("Verdana", Font.PLAIN, 11));
        txtEmail.setBounds(5, 63, 187, 27);
        panel.add(txtEmail);
        txtEmail.setColumns(10);
        
        JTextArea txtrLogin = new JTextArea();
        txtrLogin.setEditable(false);
        txtrLogin.setBounds(5, 0, 419, 44);
        txtrLogin.setBackground(new Color(168, 198, 250));
        txtrLogin.setFont(new Font("Monospaced", Font.BOLD, 30));
        txtrLogin.setText("Login into your account");
        panel.add(txtrLogin);
        
        JButton btnNewButton = new JButton("Enter");
        btnNewButton.setBounds(5, 158, 89, 23);
        panel.add(btnNewButton);
        btnNewButton.addActionListener(e -> {
            String email = txtEmail.getText();
            String password = new String(passwordField.getPassword());

            if(validateLogin(email, password)) {
            	JOptionPane.showMessageDialog(this,
                        "Login successful! Welcome back!",
                        "Login Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                        
                Session.setCurrentUserEmail(email);
                this.setVisible(false);
                new HomePage().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid email or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JTextArea txtrtheJournyeOf = new JTextArea();
        txtrtheJournyeOf.setEditable(false);
        txtrtheJournyeOf.setBackground(new Color(168, 198, 250));
        txtrtheJournyeOf.setFont(new Font("Segoe UI Light", Font.BOLD, 18));
        txtrtheJournyeOf.setText("\"The journey of a thousand \r\nmiles begins with one step\" \r\n- Lao Tuz");
        txtrtheJournyeOf.setBounds(151, 346, 236, 106);
        panel.add(txtrtheJournyeOf);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(5, 120, 187, 27);
        panel.add(passwordField);
        
        JButton btnNewButton_1 = new JButton("Forgot your password?");
        btnNewButton_1.setBackground(new Color(168, 198, 250));
        btnNewButton_1.setHorizontalAlignment(SwingConstants.LEFT);
        btnNewButton_1.setBounds(5, 192, 187, 23);
        panel.add(btnNewButton_1);
        btnNewButton_1.addActionListener(e -> {
            this.setVisible(false);
            new PasswordRecovery().setVisible(true);
        });
        
        JButton btnNewButton_1_1 = new JButton("Don't have an account? Sign in here");
        btnNewButton_1_1.addActionListener(e -> {
            this.setVisible(false);
            new SignInPage().setVisible(true);
        });
        
        btnNewButton_1_1.setHorizontalAlignment(SwingConstants.LEFT);
        btnNewButton_1_1.setBackground(new Color(168, 198, 250));
        btnNewButton_1_1.setBounds(5, 226, 255, 23);
        panel.add(btnNewButton_1_1);
        
        JLabel lblNewLabel_1 = new JLabel("Email");
        lblNewLabel_1.setBounds(5, 44, 46, 14);
        panel.add(lblNewLabel_1);
        
        JLabel lblNewLabel_1_1 = new JLabel("Password");
        lblNewLabel_1_1.setBounds(5, 101, 65, 14);
        panel.add(lblNewLabel_1_1);
        
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setIcon(new ImageIcon("G:\\My Drive\\Computer science IA\\Plannify\\src\\e7133d8303bd92e7bea4e76c43d36bc1.jpg"));
        lblNewLabel.setBounds(0, 0, 495, 586);
        contentPane.add(lblNewLabel);
    }
}