import java.io.*;
import java.util.*;
import javax.swing.*;
import java.text.SimpleDateFormat;

public class File_Utils {
    private static final File appFolder = new File("G:\\My Drive\\Computer science IA\\Plannify\\user_files");

    public static class UserPreferences {
        private List<String> equipment = new ArrayList<>();
        private List<String> goal = new ArrayList<>();
        private List<String> availabilityHome = new ArrayList<>();
        private List<String> availabilityGym = new ArrayList<>();
        
        public List<String> getEquipment() { return equipment; }
        public List<String> getGoal() { return goal; }
        public List<String> getAvailabilityHome() { return availabilityHome; }
        public List<String> getAvailabilityGym() { return availabilityGym; }
        
        public void setEquipment(List<String> equipment) { this.equipment = equipment; }
        public void setGoal(List<String> goal) { this.goal = goal; }
        public void setAvailabilityHome(List<String> availabilityHome) { this.availabilityHome = availabilityHome; }
        public void setAvailabilityGym(List<String> availabilityGym) { this.availabilityGym = availabilityGym; }
    }

    // Gets main user file (CURRENT preferences only)
    public static File getUserFile() {
        String safeFileName = Session.getSafeFileName();
        return new File(appFolder, safeFileName);
    }
    
    // Gets history file for user
    public static File getUserHistoryFile() {
        String safeFileName = Session.getSafeFileName();
        String historyFileName = safeFileName.replace(".txt", "_history.txt");
        return new File(appFolder, historyFileName);
    }

    // Reads CURRENT preferences from main file
    public static UserPreferences readCurrentPreferences(File file) {
        UserPreferences prefs = new UserPreferences();
        
        if (!file.exists()) {
            return prefs;
        }

        try (Scanner sc = new Scanner(file)) {
            String currentSection = null;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                
                if (line.endsWith(":")) {
                    currentSection = line.replace(":", "");
                } else if (currentSection != null && !line.isEmpty()) {
                    switch (currentSection) {
                        case "Equipment":
                            prefs.getEquipment().add(line);
                            break;
                        case "Goal":
                            prefs.getGoal().add(line);
                            break;
                        case "Availability_Home":
                            prefs.getAvailabilityHome().add(line);
                            break;
                        case "Availability_Gym":
                            prefs.getAvailabilityGym().add(line);
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return prefs;
    }

    // Saves preferences: 
    // 1. Overwrites main file with CURRENT preferences
    // 2. Appends to history file
    public static void savePreferences(UserPreferences prefs) {
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        
        File currentFile = getUserFile();
        File historyFile = getUserHistoryFile();
        
        // 1. Save CURRENT preferences to main file (OVERWRITE)
        saveCurrentPreferences(currentFile, prefs);
        
        // 2. Check if this is different from the last history entry
        // Only append to history if it's actually a new/changed setting
        if (!isDuplicateOfLastHistoryEntry(prefs, historyFile)) {
            appendToHistory(historyFile, prefs);
        }
    }
    private static void saveCurrentPreferences(File file, UserPreferences prefs) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            // Equipment section - only write if not empty
            if (!prefs.getEquipment().isEmpty()) {
                bw.write("Equipment:");
                bw.newLine();
                for (String item : prefs.getEquipment()) {
                    bw.write(item);
                    bw.newLine();
                }
                bw.newLine();
            }
            
            // Goal section - only write if not empty
            if (!prefs.getGoal().isEmpty()) {
                bw.write("Goal:");
                bw.newLine();
                for (String item : prefs.getGoal()) {
                    bw.write(item);
                    bw.newLine();
                }
                bw.newLine();
            }
            
            // Availability_Home section - only write if not empty
            if (!prefs.getAvailabilityHome().isEmpty()) {
                bw.write("Availability_Home:");
                bw.newLine();
                for (String item : prefs.getAvailabilityHome()) {
                    bw.write(item);
                    bw.newLine();
                }
                bw.newLine();
            }
            
            // Availability_Gym section - only write if not empty
            if (!prefs.getAvailabilityGym().isEmpty()) {
                bw.write("Availability_Gym:");
                bw.newLine();
                for (String item : prefs.getAvailabilityGym()) {
                    bw.write(item);
                    bw.newLine();
                }
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private static void appendToHistory(File historyFile, UserPreferences prefs) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(historyFile, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            
            bw.write("# Settings " + timestamp);
            bw.newLine();
            
            // ONLY write sections that have content
            boolean wroteAnything = false;
            
            // Equipment section
            if (!prefs.getEquipment().isEmpty()) {
                bw.write("Equipment:");
                bw.newLine();
                for (String item : prefs.getEquipment()) {
                    bw.write(item);
                    bw.newLine();
                }
                wroteAnything = true;
            }
            
            // Goal section
            if (!prefs.getGoal().isEmpty()) {
                if (wroteAnything) bw.newLine();
                bw.write("Goal:");
                bw.newLine();
                for (String item : prefs.getGoal()) {
                    bw.write(item);
                    bw.newLine();
                }
                wroteAnything = true;
            }
            
            // Availability_Home section
            if (!prefs.getAvailabilityHome().isEmpty()) {
                if (wroteAnything) bw.newLine();
                bw.write("Availability_Home:");
                bw.newLine();
                for (String item : prefs.getAvailabilityHome()) {
                    bw.write(item);
                    bw.newLine();
                }
                wroteAnything = true;
            }
            
            // Availability_Gym section
            if (!prefs.getAvailabilityGym().isEmpty()) {
                if (wroteAnything) bw.newLine();
                bw.write("Availability_Gym:");
                bw.newLine();
                for (String item : prefs.getAvailabilityGym()) {
                    bw.write(item);
                    bw.newLine();
                }
                wroteAnything = true;
            }
            
            // Only add spacing if we actually wrote something
            if (wroteAnything) {
                bw.newLine();
                bw.newLine();
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Reads ALL history settings from history file
    public static List<UserPreferences> readHistorySettings(File historyFile) {
        List<UserPreferences> historyList = new ArrayList<>();
        
        if (!historyFile.exists()) {
            return historyList;
        }
        
        try (Scanner sc = new Scanner(historyFile)) {
            UserPreferences currentSettings = null;
            String currentSection = null;
            
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                
                if (line.startsWith("# Settings ")) {
                    if (currentSettings != null) {
                        historyList.add(currentSettings);
                    }
                    currentSettings = new UserPreferences();
                    currentSection = null;
                } else if (line.endsWith(":")) {
                    currentSection = line.replace(":", "");
                } else if (currentSection != null && !line.isEmpty() && currentSettings != null) {
                    switch (currentSection) {
                        case "Equipment":
                            currentSettings.getEquipment().add(line);
                            break;
                        case "Goal":
                            currentSettings.getGoal().add(line);
                            break;
                        case "Availability_Home":
                            currentSettings.getAvailabilityHome().add(line);
                            break;
                        case "Availability_Gym":
                            currentSettings.getAvailabilityGym().add(line);
                            break;
                    }
                }
            }
            
            // Add the last settings
            if (currentSettings != null) {
                historyList.add(currentSettings);
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return historyList;
    }

    // MATCHING WORKOUTS - KEEP AS BEFORE
    public static List<Workout> matchWorkoutsWithUser(UserPreferences userPrefs) throws FileNotFoundException {
        List<Workout> matchedWorkouts = new ArrayList<>();
        List<Workout> allWorkouts = loadAllWorkouts();
        
        System.out.println("Total workouts in catalogue: " + allWorkouts.size());
        
        for (Workout workout : allWorkouts) {
            if (matchesUserPreferences(workout, userPrefs)) {
                matchedWorkouts.add(workout);
            }
        }
        
        System.out.println("Total matched workouts: " + matchedWorkouts.size());
        return matchedWorkouts;
    }

    private static List<Workout> loadAllWorkouts() throws FileNotFoundException {
        List<Workout> workouts = new ArrayList<>();  
        
        File workoutsFile = new File("G:\\My Drive\\Computer science IA\\Plannify\\workout_catalogue.txt");
        
        if (!workoutsFile.exists()) {
            return workouts;
        }
        
        try (Scanner sc = new Scanner(workoutsFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(",", 7);
                if (parts.length == 7) {
                    workouts.add(new Workout(
                        parts[0].trim(),      // name
                        parts[3].trim(),      // equipment
                        parts[2].trim(),      // location  
                        parts[4].trim(),      // calories
                        parts[5].trim(),      // setsReps
                        parts[6].trim(),      // rest
                        parts[1].trim()       // goal
                    ));
                }
            }
        }
        return workouts;  
    }

    private static boolean matchesUserPreferences(Workout workout, UserPreferences userPrefs) {
        // 1. Check goal
        List<String> userGoals = userPrefs.getGoal();
        if (userGoals.isEmpty()) {
            return false;
        }
        
        boolean goalMatch = false;
        for (String userGoal : userGoals) {
            if (workout.getGoal().equalsIgnoreCase(userGoal)) {
                goalMatch = true;
                break;
            }
        }
        
        if (!goalMatch) {
            return false;
        }
        
        // 2. Check location
        String location = workout.getLocation();
        if ("Home".equals(location)) {
            if (userPrefs.getAvailabilityHome().isEmpty()) {
                return false;
            }
        } else if ("Gym".equals(location)) {
            if (userPrefs.getAvailabilityGym().isEmpty()) {
                return false;
            }
        } else {
            return false;
        }
        
        // 3. Check equipment
        String equipment = workout.getEquipment();
        if ("None".equals(equipment)) {
            return true;
        }
        
        for (String userEquip : userPrefs.getEquipment()) {
            if (equipment.equalsIgnoreCase(userEquip)) {
                return true;
            }
        }
        
        return false;
    }
    private static boolean isDuplicateOfLastHistoryEntry(UserPreferences newPrefs, File historyFile) {
        if (!historyFile.exists()) {
            return false;
        }
        
        try {
            // Read the last entry from history file
            List<UserPreferences> historyList = readHistorySettings(historyFile);
            if (historyList.isEmpty()) {
                return false;
            }
            
            UserPreferences lastEntry = historyList.get(historyList.size() - 1);
            
            // Compare all fields
            return listsAreEqual(newPrefs.getEquipment(), lastEntry.getEquipment()) &&
                   listsAreEqual(newPrefs.getGoal(), lastEntry.getGoal()) &&
                   listsAreEqual(newPrefs.getAvailabilityHome(), lastEntry.getAvailabilityHome()) &&
                   listsAreEqual(newPrefs.getAvailabilityGym(), lastEntry.getAvailabilityGym());
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // NEW: Helper to compare two lists
    private static boolean listsAreEqual(List<String> list1, List<String> list2) {
        if (list1 == null && list2 == null) return true;
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;
        
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }

}