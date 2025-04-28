import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Collections;

public class BettingSystem {
    private Map<HorseGUI, Double> odds;
    private Map<HorseGUI, Double> bets;
    private double totalBets;
    private double houseBalance;
    private double playerBalance;
    private Statistics statistics;
    private Race race;

    public BettingSystem(Statistics statistics, Race race) {
        this.odds = new HashMap<>();
        this.bets = new HashMap<>();
        this.totalBets = 0;
        this.houseBalance = 10000.0; // Starting house balance
        this.playerBalance = 1000.0; // Starting player balance
        this.statistics = statistics;
        this.race = race;
    }

    public void calculateOdds(HorseGUI[] horses, Track track) {
        // Clear previous odds
        odds.clear();
        
        // Calculate base odds for each horse
        for (HorseGUI horse : horses) {
            if (horse == null) continue;
            
            // Base performance factor (0.5 to 1.5)
            double performanceFactor = 0.5 + (horse.getConfidence() * 0.5);
            
            // Recent form factor (0.8 to 1.2)
            double recentForm = calculateRecentForm(horse);
            
            // Track condition factor (0.8 to 1.2)
            double trackFactor = calculateTrackFactor(horse, track);
            
            // Weather impact factor (0.7 to 1.3)
            double weatherFactor = calculateWeatherFactor(horse, track);
            
            // Betting pattern factor (0.9 to 1.1)
            double bettingPattern = calculateBettingPattern(horse);
            
            // Calculate final odds
            double baseOdds = 10.0 / (performanceFactor * recentForm * trackFactor * weatherFactor * bettingPattern);
            
            // Apply house edge (15%)
            double finalOdds = baseOdds * 1.15;
            
            // Ensure minimum odds of 1.1
            odds.put(horse, Math.max(1.1, finalOdds));
        }
        
        // Normalize odds to ensure they sum to a reasonable total
        normalizeOdds();
    }
    
    private double calculateRecentForm(HorseGUI horse) {
        List<String> history = horse.getRaceHistory();
        if (history.isEmpty()) return 1.0;
        
        // Consider last 5 races
        int racesToConsider = Math.min(5, history.size());
        double formScore = 0.0;
        int weight = racesToConsider;
        
        for (int i = history.size() - 1; i >= Math.max(0, history.size() - racesToConsider); i--) {
            String result = history.get(i);
            double raceScore = 0.0;
            
            if (result.contains("won")) {
                raceScore = 1.2;
            } else if (result.contains("fell")) {
                raceScore = 0.8;
            } else {
                raceScore = 1.0;
            }
            
            formScore += raceScore * weight;
            weight--;
        }
        
        return 0.8 + (formScore / (racesToConsider * (racesToConsider + 1) / 2) * 0.4);
    }
    
    private double calculateTrackFactor(HorseGUI horse, Track track) {
        String trackShape = track.getShape().toLowerCase();
        String horseBreed = horse.getBreed().toLowerCase();
        
        // Breed-specific track preferences
        if (trackShape.contains("oval") && (horseBreed.contains("thoroughbred") || horseBreed.contains("arabian"))) {
            return 1.2;
        } else if (trackShape.contains("straight") && horseBreed.contains("quarter")) {
            return 1.2;
        } else if (trackShape.contains("figure-eight") && horseBreed.contains("appaloosa")) {
            return 1.2;
        }
        
        return 1.0;
    }
    
    private double calculateWeatherFactor(HorseGUI horse, Track track) {
        String weather = track.getWeatherCondition().toLowerCase();
        String horseEquipment = horse.getHorseshoes().toLowerCase() + " " + horse.getSaddle().toLowerCase();
        
        if (weather.contains("rain") || weather.contains("snow")) {
            if (horseEquipment.contains("heavy") || horseEquipment.contains("racing")) {
                return 1.1;
            } else if (horseEquipment.contains("lightweight")) {
                return 0.9;
            }
        } else if (weather.contains("clear")) {
            if (horseEquipment.contains("lightweight") || horseEquipment.contains("racing")) {
                return 1.1;
            }
        }
        
        return 1.0;
    }
    
    private double calculateBettingPattern(HorseGUI horse) {
        double totalBets = getTotalBetAmount();
        if (totalBets == 0) return 1.0;
        
        double horseBets = getBets().getOrDefault(horse, 0.0);
        double betRatio = horseBets / totalBets;
        
        // If more than 40% of total bets are on this horse, increase odds
        if (betRatio > 0.4) {
            return 1.0 + (betRatio - 0.4) * 0.5;
        }
        
        return 1.0;
    }
    
    private void normalizeOdds() {
        // Ensure minimum odds spread
        double minOdds = Double.MAX_VALUE;
        double maxOdds = Double.MIN_VALUE;
        
        for (double odd : odds.values()) {
            minOdds = Math.min(minOdds, odd);
            maxOdds = Math.max(maxOdds, odd);
        }
        
        if (maxOdds - minOdds < 2.0) {
            double adjustment = (2.0 - (maxOdds - minOdds)) / 2.0;
            for (HorseGUI horse : odds.keySet()) {
                double currentOdd = odds.get(horse);
                if (currentOdd == maxOdds) {
                    odds.put(horse, currentOdd + adjustment);
                } else if (currentOdd == minOdds) {
                    odds.put(horse, Math.max(1.1, currentOdd - adjustment));
                }
            }
        }
    }
    
    public String getBettingFeedback(HorseGUI horse, double betAmount) {
        StringBuilder feedback = new StringBuilder();
        
        // Recent performance analysis
        List<String> history = horse.getRaceHistory();
        if (!history.isEmpty()) {
            int wins = 0;
            int falls = 0;
            for (String result : history) {
                if (result.contains("won")) wins++;
                if (result.contains("fell")) falls++;
            }
            
            feedback.append("Recent Performance:\n");
            feedback.append(String.format("- Win Rate: %.1f%%\n", (double)wins/history.size() * 100));
            feedback.append(String.format("- Fall Rate: %.1f%%\n", (double)falls/history.size() * 100));
        }
        
        // Track and weather suitability
        feedback.append("\nCurrent Conditions:\n");
        feedback.append(String.format("- Track Shape: %s\n", race.getTrack().getShape()));
        feedback.append(String.format("- Weather: %s\n", race.getTrack().getWeatherCondition()));
        
        // Equipment analysis
        feedback.append("\nEquipment Analysis:\n");
        feedback.append(String.format("- Saddle: %s\n", horse.getSaddle()));
        feedback.append(String.format("- Horseshoes: %s\n", horse.getHorseshoes()));
        
        // Betting pattern analysis
        double totalBets = getTotalBetAmount();
        if (totalBets > 0) {
            double horseBets = getBets().getOrDefault(horse, 0.0);
            double betPercentage = (horseBets / totalBets) * 100;
            feedback.append(String.format("\nBetting Trends:\n- %.1f%% of total bets are on this horse\n", betPercentage));
        }
        
        // Risk assessment
        double odds = getOdds(horse);
        feedback.append("\nRisk Assessment:\n");
        if (odds < 2.0) {
            feedback.append("- Low risk, low reward bet\n");
        } else if (odds < 4.0) {
            feedback.append("- Moderate risk, potential good return\n");
        } else {
            feedback.append("- High risk, high reward bet\n");
        }
        
        // Potential payout
        double potentialWin = betAmount * odds;
        feedback.append(String.format("\nPotential Payout: $%.2f\n", potentialWin));
        
        return feedback.toString();
    }

    public boolean placeBet(HorseGUI horse, double amount) {
        if (horse == null || amount <= 0 || amount > playerBalance) {
            return false;
        }
        
        // Standard minimum bet of $2
        if (amount < 2.0) {
            return false;
        }
        
        double currentBet = bets.getOrDefault(horse, 0.0);
        bets.put(horse, currentBet + amount);
        totalBets += amount;
        playerBalance -= amount;
        return true;
    }

    public void processRaceResult(HorseGUI winningHorse) {
        if (winningHorse == null) {
            // If no winner (all horses fell), refund all bets
            playerBalance += totalBets;
            return;
        }
        
        // Calculate odds if they haven't been calculated yet
        if (odds == null || odds.isEmpty()) {
            HorseGUI[] horses = race.getHorses().toArray(new HorseGUI[0]);
            calculateOdds(horses, race.getTrack());
        }
        
        double winningBet = bets.getOrDefault(winningHorse, 0.0);
        double horseOdds = odds.getOrDefault(winningHorse, 1.0);
        double payout = winningBet * horseOdds; // Original bet + winnings
        
        // Add the original bet back plus winnings
        playerBalance += winningBet + payout;
        houseBalance += totalBets - (winningBet + payout);
        
        // Record betting statistics
        if (statistics != null) {
            statistics.recordBettingStats(winningHorse, totalBets, payout);
        }
        
        // Clear bets for next race
        bets.clear();
        totalBets = 0;
    }

    public double getOdds(HorseGUI horse) {
        return odds.getOrDefault(horse, 0.0);
    }

    public double getPlayerBalance() {
        return playerBalance;
    }

    public Map<HorseGUI, Double> getBets() {
        return new HashMap<>(bets); // Return a copy to prevent external modification
    }

    public String getBettingSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("\nBetting Summary:\n");
        summary.append("----------------\n");
        summary.append(String.format("Your Balance: $%.2f\n", playerBalance));
        summary.append(String.format("Total Bets Placed: $%.2f\n", totalBets));
        
        // Show current bets for each horse
        summary.append("\nCurrent Bets:\n");
        boolean hasBets = false;
        for (Map.Entry<HorseGUI, Double> entry : bets.entrySet()) {
            if (entry.getValue() > 0) {
                summary.append(String.format("%s: $%.2f at %.2f:1 odds\n", 
                    entry.getKey().getName(),
                    entry.getValue(),
                    odds.get(entry.getKey())));
                hasBets = true;
            }
        }
        if (!hasBets) {
            summary.append("No bets placed yet\n");
        }
        
        summary.append("\nCurrent Odds:\n");
        for (Map.Entry<HorseGUI, Double> entry : odds.entrySet()) {
            summary.append(String.format("%s: %.2f:1\n", 
                entry.getKey().getName(), 
                entry.getValue()));
        }
        return summary.toString();
    }

    private Map<HorseGUI, Double> calculateDynamicOdds(HorseGUI[] horses, Track track) {
        Map<HorseGUI, Double> odds = new HashMap<>();
        double totalImpliedProbability = 0;
        
        // Calculate base probabilities
        for (HorseGUI horse : horses) {
            if (horse != null) {
                // Base probability from horse attributes
                double baseProbability = horse.getConfidence() * horse.getSpeed() * horse.getStamina();
                
                // Track condition modifiers
                baseProbability *= track.getWeatherConfidenceModifier();
                baseProbability *= track.getTrackShapeModifier();
                
                // Recent form modifier (last 3 races)
                baseProbability *= calculateRecentFormModifier(horse);
                
                // Track record modifier
                baseProbability *= calculateTrackRecordModifier(horse, track);
                
                // Convert to implied probability
                double impliedProbability = 1.0 / (baseProbability * 2.0);
                totalImpliedProbability += impliedProbability;
                
                odds.put(horse, impliedProbability);
            }
        }
        
        // Normalize probabilities and apply house edge
        double houseEdge = 0.15; // 15% house edge
        for (Map.Entry<HorseGUI, Double> entry : odds.entrySet()) {
            double normalizedProbability = entry.getValue() / totalImpliedProbability;
            double decimalOdds = (1.0 / normalizedProbability) * (1.0 - houseEdge);
            entry.setValue(decimalOdds - 1.0); // Convert to X:1 format
        }
        
        return odds;
    }

    private double calculateRecentFormModifier(HorseGUI horse) {
        List<String> recentRaces = horse.getRecentRaceResults();
        if (recentRaces.isEmpty()) return 1.0;
        
        double formModifier = 1.0;
        int wins = 0;
        int falls = 0;
        
        for (String race : recentRaces) {
            if (race.contains("won")) wins++;
            if (race.contains("fell")) falls++;
        }
        
        formModifier += (wins * 0.1); // +10% per win
        formModifier -= (falls * 0.15); // -15% per fall
        
        return Math.max(0.5, Math.min(1.5, formModifier)); // Cap between 0.5 and 1.5
    }

    private double calculateTrackRecordModifier(HorseGUI horse, Track track) {
        String trackShape = track.getShape();
        double bestTime = horse.getBestTimeForTrack(trackShape);
        double averageTime = track.getAverageTimeForShape(trackShape);
        
        if (bestTime == 0 || averageTime == 0) return 1.0;
        
        // If horse's best time is better than average, increase probability
        double timeRatio = averageTime / bestTime;
        return Math.max(0.8, Math.min(1.2, timeRatio));
    }

    public void updateOddsDuringRace(HorseGUI[] horses, Track track) {
        // Update odds based on current race progress
        Map<HorseGUI, Double> newOdds = calculateDynamicOdds(horses, track);
        
        // Apply momentum modifier based on current position
        for (HorseGUI horse : horses) {
            if (horse != null) {
                double positionRatio = horse.getDistanceTravelled() / track.getLength();
                double momentumModifier = 1.0 + (positionRatio * 0.2); // Up to +20% for leading
                newOdds.put(horse, newOdds.get(horse) * momentumModifier);
            }
        }
        
        this.odds = newOdds;
    }

    public String getBettingSuggestion() {
        if (bets.isEmpty()) return "No betting history available for suggestions.";
        
        // Analyze betting patterns
        Map<HorseGUI, Integer> horseBets = new HashMap<>();
        double totalBets = 0;
        double totalWins = 0;
        
        for (Map.Entry<HorseGUI, Double> entry : bets.entrySet()) {
            horseBets.merge(entry.getKey(), 1, Integer::sum);
            totalBets++;
            if (entry.getKey().getWins() > 0) totalWins++;
        }
        
        // Calculate win rate
        double winRate = totalWins / totalBets;
        
        // Find most and least bet horses
        HorseGUI mostBet = Collections.max(horseBets.entrySet(), 
            Map.Entry.comparingByValue()).getKey();
        HorseGUI leastBet = Collections.min(horseBets.entrySet(), 
            Map.Entry.comparingByValue()).getKey();
        
        // Generate suggestion
        StringBuilder suggestion = new StringBuilder();
        suggestion.append("Based on your betting history:\n");
        suggestion.append(String.format("- Overall win rate: %.1f%%\n", winRate * 100));
        suggestion.append(String.format("- Most bet horse: %s (%d times)\n", 
            mostBet.getName(), horseBets.get(mostBet)));
        suggestion.append(String.format("- Least bet horse: %s (%d times)\n", 
            leastBet.getName(), horseBets.get(leastBet)));
        
        if (winRate < 0.3) {
            suggestion.append("\nSuggestion: Consider betting on horses with higher odds for better payouts.");
        } else if (winRate > 0.7) {
            suggestion.append("\nSuggestion: Your strategy is working well! Consider increasing bet amounts.");
        } else {
            suggestion.append("\nSuggestion: Try diversifying your bets across different horses.");
        }
        
        return suggestion.toString();
    }

    private double getTotalBetAmount() {
        double total = 0;
        for (double bet : bets.values()) {
            total += bet;
        }
        return total;
    }
} 