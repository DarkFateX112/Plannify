public class Session {
 private static String currentUserEmail = null;

 public static void setCurrentUserEmail(String email) {
     currentUserEmail = email;
 }

 public static String getCurrentUserEmail() {
     return currentUserEmail;
 }

 public static String getSafeFileName() {
     if (currentUserEmail == null) {
         return "default_user.txt";
     }
     return currentUserEmail.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
 }

 public static void logout() {
     currentUserEmail = null;
 }
}