import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class WorkoutDistributor {
    
    public static void distributeWorkouts(File_Utils.UserPreferences prefs, List<Workout> matchedWorkouts) {
        String userEmail = Session.getCurrentUserEmail();
        if (userEmail == null) {
            return;
        }
        
        File appFolder = new File("G:\\My Drive\\Computer science IA\\Plannify\\user_files");
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        
        String safeFileName = userEmail.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        File userWorkoutFile = new File(appFolder, "daily_schedule_" + safeFileName);
        
        // Get availability days
        List<String> homeDays = prefs.getAvailabilityHome();
        List<String> gymDays = prefs.getAvailabilityGym();
        
        // Separate workouts by location
        List<Workout> homeWorkouts = new ArrayList<>();
        List<Workout> gymWorkouts = new ArrayList<>();
        
        for (Workout workout : matchedWorkouts) {
            if ("Home".equals(workout.getLocation())) {
                homeWorkouts.add(workout);
            } else if ("Gym".equals(workout.getLocation())) {
                gymWorkouts.add(workout);
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userWorkoutFile))) {
            writer.write("# DAILY WORKOUT SCHEDULE");
            writer.newLine();
            writer.write("# Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.newLine();
            writer.write("# User: " + userEmail);
            writer.newLine();
            writer.newLine();
            
            // Distribute home workouts
            if (!homeDays.isEmpty() && !homeWorkouts.isEmpty()) {
                writer.write("# HOME WORKOUTS");
                writer.newLine();
                distributeToDays(writer, homeWorkouts, homeDays, "Home");
            }
            
            // Distribute gym workouts
            if (!gymDays.isEmpty() && !gymWorkouts.isEmpty()) {
                writer.write("# GYM WORKOUTS");
                writer.newLine();
                distributeToDays(writer, gymWorkouts, gymDays, "Gym");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void distributeToDays(BufferedWriter writer, 
            List<Workout> workouts, List<String> days, String location) throws IOException {
        
        Collections.shuffle(workouts);
        
        for (String day : days) {
            writer.write("## " + day.toUpperCase() + " (" + location + ")");
            writer.newLine();
            
            // Show minimum 10 workouts per day
            int workoutsToShow = Math.max(10, workouts.size());
            
            for (int i = 0; i < workoutsToShow; i++) {
                
                Workout workout = workouts.get(i % workouts.size());
                writer.write(workout.getName() + " | ");
                writer.write("Goal: " + workout.getGoal() + " | ");
                writer.write("Sets/Reps: " + workout.getSetsReps() + " | ");
                writer.write("Rest: " + workout.getRest() + " | ");
                writer.write("Calories: " + workout.getCalories() + " kcal");
                writer.newLine();
            }
            writer.newLine();
        }
    }
    
    public static List<String> getWorkoutsForDay(String day) throws IOException {
        String userEmail = Session.getCurrentUserEmail();
        if (userEmail == null) {
            return new ArrayList<>();
        }
        
        File appFolder = new File("G:\\My Drive\\Computer science IA\\Plannify\\user_files");
        String safeFileName = userEmail.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        File userWorkoutFile = new File(appFolder, "daily_schedule_" + safeFileName);
        
        if (!userWorkoutFile.exists()) {
            return new ArrayList<>();
        }
        
        List<String> workouts = new ArrayList<>();
        boolean foundDay = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(userWorkoutFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.startsWith("## " + day.toUpperCase())) {
                    foundDay = true;
                    continue;
                }
                
                if (foundDay) {
                    if (line.startsWith("## ")) {
                        break;
                    }
                    if (!line.isEmpty() && !line.startsWith("#") && line.contains("|")) {
                        workouts.add(line);
                    }
                }
            }
        }
        
        return workouts;
    }
}