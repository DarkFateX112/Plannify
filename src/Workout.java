public class Workout {
    private String name;
    private String equipment;
    private String location;
    private String calories;  // ADD THIS FIELD
    private String setsReps;
    private String rest;
    private String goal;
    
    // Update constructor to include calories
    public Workout(String name, String equipment, String location, 
                   String calories, String setsReps, String rest, String goal) {
        this.name = name;
        this.equipment = equipment;
        this.location = location;
        this.calories = calories;
        this.setsReps = setsReps;
        this.rest = rest;
        this.goal = goal;
    }
    
    // Add getter for calories
    public String getName() {return name;}
    public String getCalories() {return calories;}
    public String getGoal() { return goal; }
    public String getLocation() { return location; }
    public String getEquipment() { return equipment; }
    public String getSetsReps() { return setsReps; }
    public String getRest() { return rest; }
}