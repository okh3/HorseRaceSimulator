import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    private Map<HorseGUI, List<RaceStats>> horseStats;
    private Map<String, Double> trackRecords;
    
    public Statistics() {
        this.horseStats = new HashMap<>();
        this.trackRecords = new HashMap<>();
    }
    
    public void recordRace(HorseGUI horse, RaceStats stats) {
        if (!horseStats.containsKey(horse)) {
            horseStats.put(horse, new ArrayList<>());
        }
        horseStats.get(horse).add(stats);
        
        // Update track record if this is the fastest time
        String trackKey = stats.getTrackName() + "_" + stats.getWeatherCondition();
        double currentTime = stats.getFinishingTime();
        if (!trackRecords.containsKey(trackKey) || currentTime < trackRecords.get(trackKey)) {
            trackRecords.put(trackKey, currentTime);
        }
    }
    
    public double getAverageSpeed(HorseGUI horse) {
        List<RaceStats> stats = horseStats.get(horse);
        if (stats == null || stats.isEmpty()) {
            return 0.0;
        }
        
        double totalSpeed = 0;
        for (RaceStats stat : stats) {
            totalSpeed += stat.getAverageSpeed();
        }
        return totalSpeed / stats.size();
    }
    
    public double getWinRatio(HorseGUI horse) {
        List<RaceStats> stats = horseStats.get(horse);
        if (stats == null || stats.isEmpty()) {
            return 0.0;
        }
        
        int wins = 0;
        for (RaceStats stat : stats) {
            if (stat.isWinner()) {
                wins++;
            }
        }
        return (double) wins / stats.size();
    }
    
    public double getTrackRecord(String trackName, String weatherCondition) {
        String key = trackName + "_" + weatherCondition;
        return trackRecords.getOrDefault(key, 0.0);
    }
    
    public List<RaceStats> getRaceHistory(HorseGUI horse) {
        return horseStats.getOrDefault(horse, new ArrayList<>());
    }
    
    public double getConfidenceTrend(HorseGUI horse) {
        List<RaceStats> stats = horseStats.get(horse);
        if (stats == null || stats.size() < 2) {
            return 0.0;
        }
        
        // Calculate the trend in confidence changes
        double totalChange = 0;
        for (int i = 1; i < stats.size(); i++) {
            totalChange += stats.get(i).getFinalConfidence() - 
                         stats.get(i-1).getFinalConfidence();
        }
        return totalChange / (stats.size() - 1);
    }
    
    public String generatePerformanceReport(HorseGUI horse) {
        StringBuilder report = new StringBuilder();
        report.append("Performance Report for ").append(horse.getName()).append("\n");
        report.append("----------------------------------------\n");
        report.append("Average Speed: ").append(String.format("%.2f", getAverageSpeed(horse))).append("\n");
        report.append("Win Ratio: ").append(String.format("%.2f", getWinRatio(horse) * 100)).append("%\n");
        report.append("Confidence Trend: ").append(String.format("%.2f", getConfidenceTrend(horse))).append("\n");
        
        report.append("\nRecent Races:\n");
        List<RaceStats> recentRaces = getRaceHistory(horse);
        for (RaceStats race : recentRaces) {
            report.append("- ").append(race.getTrackName())
                  .append(" (").append(race.getWeatherCondition()).append("): ")
                  .append(String.format("%.2f", race.getFinishingTime()))
                  .append(" seconds\n");
        }
        
        return report.toString();
    }

    public void recordBettingStats(HorseGUI winningHorse, double totalBets, double payout) {
        // Update the horse's race stats with betting information
        List<RaceStats> horseStats = this.horseStats.get(winningHorse);
        if (horseStats != null && !horseStats.isEmpty()) {
            RaceStats lastRace = horseStats.get(horseStats.size() - 1);
            // We can store betting information in the RaceStats class if needed
            // For now, we'll just update the confidence based on the betting outcome
            double confidenceChange = (payout > 0) ? 0.1 : -0.1;
            lastRace.setFinalConfidence(lastRace.getFinalConfidence() + confidenceChange);
        }
    }
}

class RaceStats {
    private String trackName;
    private String weatherCondition;
    private double finishingTime;
    private double averageSpeed;
    private double initialConfidence;
    private double finalConfidence;
    private boolean isWinner;
    
    public RaceStats(String trackName, String weatherCondition, double finishingTime,
                    double averageSpeed, double initialConfidence, double finalConfidence,
                    boolean isWinner) {
        this.trackName = trackName;
        this.weatherCondition = weatherCondition;
        this.finishingTime = finishingTime;
        this.averageSpeed = averageSpeed;
        this.initialConfidence = initialConfidence;
        this.finalConfidence = finalConfidence;
        this.isWinner = isWinner;
    }
    
    public String getTrackName() {
        return trackName;
    }
    
    public String getWeatherCondition() {
        return weatherCondition;
    }
    
    public double getFinishingTime() {
        return finishingTime;
    }
    
    public double getAverageSpeed() {
        return averageSpeed;
    }
    
    public double getInitialConfidence() {
        return initialConfidence;
    }
    
    public double getFinalConfidence() {
        return finalConfidence;
    }
    
    public boolean isWinner() {
        return isWinner;
    }
    
    public void setFinalConfidence(double finalConfidence) {
        this.finalConfidence = finalConfidence;
    }
} 