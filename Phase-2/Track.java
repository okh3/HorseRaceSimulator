import java.util.HashMap;
import java.util.Map;

public class Track {
    private int length;
    private int numberOfLanes;
    private String shape;
    private String weatherCondition;
    private double weatherSpeedModifier;
    private double weatherConfidenceModifier;
    private double weatherFallChanceModifier;
    private Map<String, Double> averageTimes;
    
    public Track(int length, int numberOfLanes) {
        this.length = length;
        this.numberOfLanes = numberOfLanes;
        this.shape = "Oval"; // Default shape
        this.weatherCondition = "Clear"; // Default weather
        this.averageTimes = new HashMap<>();
        // Initialize with some default average times
        averageTimes.put("Oval", 15.0);
        averageTimes.put("Figure-eight", 18.0);
        averageTimes.put("Straight", 12.0);
        averageTimes.put("Zigzag", 20.0);
        averageTimes.put("Custom", 25.0);
        updateWeatherModifiers();
    }
    
    public void setShape(String shape) {
        this.shape = shape;
    }
    
    public void setWeatherCondition(String condition) {
        this.weatherCondition = condition;
        updateWeatherModifiers();
    }
    
    private void updateWeatherModifiers() {
        switch (weatherCondition) {
            case "Clear":
                weatherSpeedModifier = 1.0;
                weatherConfidenceModifier = 1.0;
                weatherFallChanceModifier = 1.0;
                break;
            case "Rainy":
                weatherSpeedModifier = 0.8;
                weatherConfidenceModifier = 0.9;
                weatherFallChanceModifier = 1.2;
                break;
            case "Snowy":
                weatherSpeedModifier = 0.6;
                weatherConfidenceModifier = 0.8;
                weatherFallChanceModifier = 1.5;
                break;
            default:
                weatherSpeedModifier = 1.0;
                weatherConfidenceModifier = 1.0;
                weatherFallChanceModifier = 1.0;
        }
    }
    
    public int getLength() {
        return length;
    }
    
    public int getNumberOfLanes() {
        return numberOfLanes;
    }
    
    public String getShape() {
        return shape;
    }
    
    public String getWeatherCondition() {
        return weatherCondition;
    }
    
    public double getWeatherSpeedModifier() {
        return weatherSpeedModifier;
    }
    
    public double getWeatherConfidenceModifier() {
        return weatherConfidenceModifier;
    }
    
    public double getWeatherFallChanceModifier() {
        return weatherFallChanceModifier;
    }

    public double getTrackShapeModifier() {
        switch (shape.toLowerCase()) {
            case "oval":
                return 1.0;  // Standard speed
            case "figure-eight":
                return 0.9;  // Slightly slower due to turns
            case "custom":
                return 0.8;  // Slowest due to complex layout
            default:
                return 1.0;
        }
    }

    public double getAverageTimeForShape(String shape) {
        return averageTimes.getOrDefault(shape, 20.0); // Default to 20 seconds if shape not found
    }
    
    public void updateAverageTime(String shape, double time) {
        double currentAverage = averageTimes.getOrDefault(shape, time);
        // Update with a weighted average (70% old, 30% new)
        double newAverage = (currentAverage * 0.7) + (time * 0.3);
        averageTimes.put(shape, newAverage);
    }
} 