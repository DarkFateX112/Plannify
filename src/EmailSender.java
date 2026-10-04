import javax.swing.*;

public class EmailSender {
    
    public static boolean sendPasswordResetEmail(String recipientEmail, String resetLink) {
        // For now, simulate email sending
        System.out.println("Simulating email to: " + recipientEmail);
        System.out.println("Reset link: " + resetLink);
        
        // Show a message to the user since we can't send real emails
        JOptionPane.showMessageDialog(null,
            "Password reset link would be sent to: " + recipientEmail + "\n" +
            "Reset link: " + resetLink + "\n\n" +
            "(In production, this would be a real email)",
            "Password Reset Simulation",
            JOptionPane.INFORMATION_MESSAGE);
        
        return true;
    }
    
    public static String generateResetLink(String email) {
        // Generate a simple reset token
        String token = email.hashCode() + "_" + System.currentTimeMillis();
        return "plannify://reset?email=" + email + "&token=" + token;
    }
}