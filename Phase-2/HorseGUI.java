import java.util.ArrayList;
import java.util.List;

public class HorseGUI extends Horse {
    private String breed;
    private String coatColor;
    private String equipment;
    private double speedModifier;
    private double confidenceModifier;
    private String symbol;
    private String saddle;
    private String horseshoes;
    private double baseSpeed;
    private double baseConfidence;
    private double baseStamina;
    private double speed;
    private double stamina;
    private List<String> raceHistory;
    private int wins;
    private double totalDistance;
    private double totalTime;
    
    public HorseGUI(char horseSymbol, String horseName, double horseConfidence) {
        super(horseSymbol, horseName, horseConfidence);
        this.breed = "Thoroughbred"; // Default breed
        this.coatColor = "Brown"; // Default color
        this.equipment = "Standard"; // Default equipment
        this.speedModifier = 1.0;
        this.confidenceModifier = 1.0;
        this.symbol = String.valueOf(horseSymbol);
        this.saddle = "Standard";
        this.horseshoes = "Standard";
        this.baseSpeed = 1.0;
        this.baseConfidence = horseConfidence;
        this.baseStamina = 1.0;
        this.speed = 1.0;
        this.stamina = 1.0;
        this.raceHistory = new ArrayList<>();
        this.wins = 0;
        this.totalDistance = 0;
        this.totalTime = 0;
    }
    
    public void setBreed(String breed) {
        this.breed = breed;
        updateAttributes();
    }
    
    public void setCoatColor(String color) {
        this.coatColor = color;
    }
    
    public void setEquipment(String equipment) {
        this.equipment = equipment;
        updateModifiers();
    }
    
    public void setSaddle(String saddle) {
        this.saddle = saddle;
        updateAttributes();
    }
    
    public void setHorseshoes(String horseshoes) {
        this.horseshoes = horseshoes;
        updateAttributes();
    }
    
    private void updateModifiers() {
        // Update speed and confidence modifiers based on breed and equipment
        switch (breed) {
            case "Thoroughbred":
                speedModifier = 1.2;
                confidenceModifier = 0.9;
                break;
            case "Arabian":
                speedModifier = 1.1;
                confidenceModifier = 1.1;
                break;
            case "Quarter Horse":
                speedModifier = 1.0;
                confidenceModifier = 1.0;
                break;
            default:
                speedModifier = 1.0;
                confidenceModifier = 1.0;
        }
        // Adjust modifiers based on equipment
        switch (equipment) {
            case "Lightweight":
                speedModifier *= 1.1;
                confidenceModifier *= 0.9;
                break;
            case "Heavy":
                speedModifier *= 0.9;
                confidenceModifier *= 1.1;
                break;
            case "Standard":
            default:
                // No modification
                break;
        }
    }
    
    private void updateAttributes() {
        // Reset base attributes
        baseSpeed = 1.0;
        baseConfidence = getConfidence();
        baseStamina = 1.0;
        
        // Apply breed effects
        switch (breed) {
            case "Thoroughbred":
                baseSpeed *= 1.2;
                baseConfidence *= 0.9;
                baseStamina *= 0.8;
                break;
            case "Arabian":
                baseSpeed *= 1.1;
                baseConfidence *= 1.1;
                baseStamina *= 1.2;
                break;
            case "Quarter Horse":
                baseSpeed *= 1.3;
                baseConfidence *= 0.8;
                baseStamina *= 0.9;
                break;
            case "Appaloosa":
                baseSpeed *= 0.9;
                baseConfidence *= 1.0;
                baseStamina *= 1.1;
                break;
            case "Paint":
                baseSpeed *= 1.0;
                baseConfidence *= 1.0;
                baseStamina *= 1.0;
                break;
        }
        
        // Apply saddle effects
        switch (saddle) {
            case "Racing":
                baseSpeed *= 1.1;
                baseStamina *= 0.9;
                break;
            case "Lightweight":
                baseSpeed *= 1.05;
                baseStamina *= 1.05;
                break;
            case "Heavy":
                baseSpeed *= 0.9;
                baseStamina *= 1.1;
                break;
        }
        
        // Apply horseshoe effects
        switch (horseshoes) {
            case "Racing":
                baseSpeed *= 1.1;
                baseConfidence *= 0.9;
                break;
            case "Lightweight":
                baseSpeed *= 1.05;
                baseConfidence *= 1.05;
                break;
            case "Heavy":
                baseSpeed *= 0.9;
                baseConfidence *= 1.1;
                break;
        }
        
        // Update current attributes
        setSpeed(baseSpeed);
        setConfidence(baseConfidence);
        setStamina(baseStamina);
    }
    
    public String getBreed() {
        return breed;
    }
    
    public String getCoatColor() {
        return coatColor;
    }
    
    public String getEquipment() {
        return equipment;
    }
    
    public double getSpeedModifier() {
        return speedModifier;
    }
    
    public double getConfidenceModifier() {
        return confidenceModifier;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public double getStamina() {
        return stamina;
    }
    
    public void setStamina(double stamina) {
        this.stamina = stamina;
    }
    
    public List<String> getRecentRaceResults() {
        return raceHistory.subList(Math.max(0, raceHistory.size() - 3), raceHistory.size());
    }
    
    public double getBestTimeForTrack(String trackShape) {
        double bestTime = Double.MAX_VALUE;
        for (String race : raceHistory) {
            if (race.contains(trackShape)) {
                String[] parts = race.split(":");
                if (parts.length > 1) {
                    double time = Double.parseDouble(parts[1]);
                    bestTime = Math.min(bestTime, time);
                }
            }
        }
        return bestTime == Double.MAX_VALUE ? 0 : bestTime;
    }
    
    public int getWins() {
        return wins;
    }
    
    public void addWin() {
        wins++;
    }
    
    public void addRaceResult(String result) {
        raceHistory.add(result);
    }
    
    public List<String> getRaceHistory() {
        return new ArrayList<>(raceHistory);
    }
    
    public double getAverageSpeed() {
        return totalTime > 0 ? totalDistance / totalTime : 0;
    }
    
    public double getWinRatio() {
        return raceHistory.isEmpty() ? 0 : (double) wins / raceHistory.size();
    }
    
    public void updateRaceStats(double distance, double time) {
        totalDistance += distance;
        totalTime += time;
    }
    
    @Override
    public void moveForward() {
        // Apply speed modifier to movement
        for (int i = 0; i < speedModifier; i++) {
            super.moveForward();
        }
    }
    
    @Override
    public double getConfidence() {
        // Apply confidence modifier to base confidence
        return super.getConfidence() * confidenceModifier;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void setSymbol(char symbol) {
        this.symbol = String.valueOf(symbol);
        super.setSymbol(symbol);
    }

    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public char getSymbol() {
        return symbol.charAt(0);
    }

    public String getSaddle() {
        return saddle;
    }
    
    public String getHorseshoes() {
        return horseshoes;
    }
} 