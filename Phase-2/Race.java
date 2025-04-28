import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.Container;
import java.util.Map;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private HashMap<Integer, Horse> horseMap;
    private Horse winnerHorse;
    private Track track;
    private Statistics statistics;
    private BettingSystem bettingSystem;
    private List<HorseGUI> horses;
    private long raceStartTime;
    private JTextArea raceDisplay;
    private Timer raceTimer;
    private int laneCount;
    private int trackLength;
    private Map<String, Double> bestTimes;
    private List<String> recentBets;
    private int totalBets;
    private double totalBetAmount;
    private int winningBets;
    private boolean raceRunning = false;
    private boolean racePaused = false;
    private Runnable raceEndListener = null;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, JTextArea raceDisplay)
    {
        // initialise instance variables
        raceLength = distance;
        this.raceDisplay = raceDisplay;

        horseMap  = new HashMap<>();

        // Adjusted confidence values for more balanced races
        // Using simple ASCII special characters that work in all terminals
        horseMap.put(0, new HorseGUI('>', " Le Horse", 0.7));      // Arrow for Knight - Good all-rounder
        horseMap.put(1, new HorseGUI('^', " Solider", 0.65));      // Arrow for Soldier - Slightly less consistent
        horseMap.put(2, new HorseGUI('#', " The Castle", 0.75));   // Hash for Castle - Strong but can be unpredictable
        horseMap.put(3, new HorseGUI('*', " King", 0.68));         // Star for King - Balanced performer

        this.track = new Track(distance, 4); // Default to 4 lanes
        this.statistics = new Statistics();
        this.bettingSystem = new BettingSystem(statistics, this);
        this.horses = new ArrayList<>();
        
        for (Horse horse : horseMap.values()) {
            if (horse != null && horse instanceof HorseGUI) {
                horses.add((HorseGUI) horse);
            }
        }

        // Create timer for race updates
        raceTimer = new Timer(100, e -> {
            if (raceRunning && !racePaused) {
                updateRace();
            }
        });

        this.laneCount = 4;
        this.bestTimes = new HashMap<>();
        this.recentBets = new ArrayList<>();
        this.totalBets = 0;
        this.totalBetAmount = 0;
        this.winningBets = 0;
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if(horseMap == null){
            return;
        }

        horseMap.putIfAbsent(laneNumber, theHorse);
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        if (!raceRunning) {
            raceRunning = true;
            racePaused = false;
            raceStartTime = System.nanoTime();
            // Reset horse positions but preserve their attributes
            for (Horse horse : horseMap.values()) {
                if (horse != null) {
                    horse.setIsWinner(false);
                    horse.goBackToStart();
                }
            }
            winnerHorse = null;
            raceTimer.start();
        } else if (racePaused) {
            racePaused = false;
            raceTimer.start();
        }
    }

    private void updateRace() {
        // Move horses
        for (Horse horse : horseMap.values()) {
            if (horse != null) {
                moveHorse(horse);
            }
        }

        // Update display
        printRace();

        // Check for winner
        if (raceWonBy()) {
            raceRunning = false;
            raceTimer.stop();
            processRaceResults();
            if (raceEndListener != null) {
                raceEndListener.run();
            }
            return;
        }
        // Check if all horses have fallen
        else if (isAllHorsesFallen()) {
            raceRunning = false;
            raceTimer.stop();
            processRaceResults();
            if (raceEndListener != null) {
                raceEndListener.run();
            }
            return;
        }
    }

    public void stopRace() {
        if (raceRunning) {
            racePaused = true;
            raceTimer.stop();
        }
    }

    public void resetRace() {
        raceRunning = false;
        racePaused = false;
        raceTimer.stop();
        // Reset all horses
        for (Horse horse : horseMap.values()) {
            if (horse != null) {
                horse.setIsWinner(false);
                horse.goBackToStart();
            }
        }
        winnerHorse = null;
        printRace();
    }

    private void processRaceResults() {
        // Calculate race statistics
        double raceTime = (System.nanoTime() - raceStartTime) / 1_000_000_000.0;
        
        // Record stats for all horses
        for (Horse horse : horseMap.values()) {
            if (horse != null && horse instanceof HorseGUI) {
                HorseGUI horseGUI = (HorseGUI) horse;
                
                // Calculate individual horse time
                double horseTime = raceTime;
                if (horse.hasFallen()) {
                    // If horse fell, use the actual time it fell
                    horseTime = raceTime;
                } else if (horse.getDistanceTravelled() < raceLength) {
                    // If horse didn't finish, calculate time based on distance covered
                    double completionRatio = (double)horse.getDistanceTravelled() / raceLength;
                    horseTime = raceTime / completionRatio;
                }
                
                double horseSpeed = raceLength / horseTime;
                
                // Update horse statistics
                if (horse == winnerHorse) {
                    horseGUI.addWin();
                }
                String resultText = track.getShape() + ": " + String.format("%.2f", horseTime);
                if (horse.hasFallen()) {
                    resultText += " (fell)";
                }
                horseGUI.addRaceResult(resultText);
                horseGUI.updateRaceStats(raceLength, horseTime);
                
                // Record race statistics
                RaceStats stats = new RaceStats(
                    track.getShape(),
                    track.getWeatherCondition(),
                    horseTime,
                    horseSpeed,
                    horseGUI.getConfidence(),
                    horseGUI.getConfidence(),
                    horse == winnerHorse
                );
                
                statistics.recordRace(horseGUI, stats);
            }
        }
        
        // Update track records
        String trackKey = track.getShape() + "_" + track.getWeatherCondition();
        if (!bestTimes.containsKey(trackKey) || raceTime < bestTimes.get(trackKey)) {
            bestTimes.put(trackKey, raceTime);
        }
        
        // Process betting results
        if (winnerHorse != null && winnerHorse instanceof HorseGUI) {
            HorseGUI winningHorse = (HorseGUI) winnerHorse;
            
            // Record betting statistics
            double totalBets = bettingSystem.getBets().values().stream().mapToDouble(Double::doubleValue).sum();
            double winningBet = bettingSystem.getBets().getOrDefault(winningHorse, 0.0);
            double winningOdds = bettingSystem.getOdds(winningHorse);
            double payout = winningBet * winningOdds;
            
            recordBet(totalBets, winningBet > 0);
            
            bettingSystem.processRaceResult(winningHorse);
            
            // Print winner and statistics
            StringBuilder results = new StringBuilder();
            results.append("\n\n=== RACE RESULTS ===\n");
            results.append("Winner: ").append(winningHorse.getName()).append("\n");
            results.append(String.format("Race Time: %.2f seconds\n", raceTime));
            results.append(String.format("Average Speed: %.2f units/second\n", raceLength / raceTime));
            results.append("Track Shape: ").append(track.getShape()).append("\n");
            results.append("Weather: ").append(track.getWeatherCondition()).append("\n");
            results.append("\nBetting Results:\n");
            
            if (winningBet > 0) {
                results.append(String.format("You bet $%.2f on %s at %.2f:1 odds\n", 
                    winningBet, winningHorse.getName(), winningOdds));
                results.append(String.format("You won $%.2f!\n", payout));
            } else {
                results.append("You didn't bet on the winning horse.\n");
            }
            
            results.append("\nBetting Summary:\n");
            results.append("----------------\n");
            results.append(String.format("Your Balance: $%.2f\n", bettingSystem.getPlayerBalance()));
            results.append(String.format("Total Bets Placed: $%.2f\n", totalBets));
            
            raceDisplay.append(results.toString());
        } else if (isAllHorsesFallen()) {
            // Store total bets before processing results
            double totalBets = bettingSystem.getBets().values().stream().mapToDouble(Double::doubleValue).sum();
            
            // Record betting statistics for no winner
            recordBet(totalBets, false);
            
            // Process betting results for no winner case
            bettingSystem.processRaceResult(null);
            
            StringBuilder results = new StringBuilder();
            results.append("\n\n=== RACE RESULTS ===\n");
            results.append("No Winner - All horses have fallen!\n");
            results.append("Track Shape: ").append(track.getShape()).append("\n");
            results.append("Weather: ").append(track.getWeatherCondition()).append("\n");
            results.append("\nBetting Results:\n");
            results.append(String.format("All bets ($%.2f) are refunded due to no winner.\n", totalBets));
            results.append("\nBetting Summary:\n");
            results.append("----------------\n");
            results.append(String.format("Your Balance: $%.2f\n", bettingSystem.getPlayerBalance()));
            results.append(String.format("Total Bets Placed: $%.2f\n", totalBets));
            
            raceDisplay.append(results.toString());
        }
        
        // Update the GUI balance label and re-enable betting controls
        SwingUtilities.invokeLater(() -> {
            if (raceDisplay instanceof JTextArea) {
                JTextArea textArea = (JTextArea) raceDisplay;
                Container parent = textArea.getParent();
                while (parent != null && !(parent instanceof MainGUI)) {
                    parent = parent.getParent();
                }
                if (parent instanceof MainGUI) {
                    MainGUI mainGUI = (MainGUI) parent;
                    mainGUI.updateBalanceLabel();
                    mainGUI.clearBettingUI();
                    mainGUI.enableBettingControls();
                }
            }
        });
        
        // Notify race end listener if set
        if (raceEndListener != null) {
            raceEndListener.run();
        }
    }

    //new introduced method to print all fallen horses and informing there is no winner.
    private void printNoWinner() {
        StringBuilder sb = new StringBuilder();
        for (Horse horse : horseMap.values()) {
            if (horse == null) continue;
            sb.append(horse.getSymbol()).append(" ").append(horse.getName()).append(" has fallen\n");
        }
        sb.append("All Horses have fallen! no Winner!\n");
        raceDisplay.append(sb.toString());
    }

    private boolean isAllHorsesFallen(){
        int fallenCount = 0;
        int nullCount = 0;

        for(Horse horse: horseMap.values()){
            if(horse == null){
                nullCount++;
                continue;
            }
            if(horse.hasFallen()){
                fallenCount++;
            }
        }

        return (fallenCount + nullCount) == horseMap.size();
    }

    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        if (!theHorse.hasFallen()) {
            // Apply weather and track condition modifiers
            double moveChance = theHorse.getConfidence() * 0.95;  // Increased base move chance
            moveChance *= track.getWeatherConfidenceModifier();
            moveChance *= track.getTrackShapeModifier();
            
            // Reduced fall chance and made it more dependent on confidence
            double fallChance = 0.01 * (1.3 - theHorse.getConfidence());
            fallChance *= track.getWeatherFallChanceModifier();
            
            if (Math.random() < moveChance) {
                theHorse.moveForward();
            }
            
            if (Math.random() < fallChance) {
                theHorse.fall();
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy()
    {
        if(raceLength == 0){
            System.out.println("Race Length is 0. There is no winner!");
            return false;
        }

        for(Horse horse: horseMap.values()){
            if(horse == null){
                continue;
            }
            if(horse.getDistanceTravelled() == raceLength){
                horse.setIsWinner(true);
                winnerHorse = horse;
                return true;
            }
        }
        return false;
    }

    //Clears the console using a more reliable method
    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
            }
            System.out.flush();
        } catch (Exception e) {
            // Fallback to printing multiple newlines if clearing fails
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /***
     * Print the race on the terminal with animation effect
     */
    public void printRace()
    {
        StringBuilder raceState = new StringBuilder();
        raceState.append("HORSE RACE IN PROGRESS!\n");
        raceState.append("======================\n\n");

        // Print the track
        multiplePrint(raceState, '=', raceLength + 3); //top edge of track
        raceState.append("\n");

        // Print each horse's lane
        for (Horse horse : horseMap.values()) {
            printLane(raceState, horse);
            raceState.append("\n");
        }

        multiplePrint(raceState, '=', raceLength + 3); //bottom edge of track
        raceState.append("\n");
        
        // Update the GUI display
        raceDisplay.setText(raceState.toString());
        raceDisplay.setCaretPosition(0); // Scroll to top
    }

    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(StringBuilder sb, Horse theHorse)
    {
        if (theHorse == null) {
            sb.append('|');
            multiplePrint(sb, ' ', raceLength);
            sb.append('|');
            sb.append(" Empty");
            return;
        }

        // Calculate how many spaces are needed before and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        // Print a | for the beginning of the lane
        sb.append('|');

        // Print the spaces before the horse
        multiplePrint(sb, ' ', spacesBefore);

        // If the horse has fallen, print fallen symbol; else print the horse's symbol
        if (theHorse.hasFallen())
        {
            sb.append('+');  // Plus sign for fallen horses
        }
        else
        {
            sb.append(theHorse.getSymbol());
        }

        // Print the spaces after the horse
        multiplePrint(sb, ' ', spacesAfter);

        // Print the | for the end of the track
        sb.append('|');

        // Print the horse's name and confidence to the right of the lane
        sb.append(String.format(" %s (Current Confidence: %.1f)", theHorse.getName(), theHorse.getConfidence()));
    }


    private void printWinner(Horse theHorse){
        if(theHorse == null) // added null check
            return;

        String winnerText = "And the winner is................  " + theHorse.getName() + "!\n";
        raceDisplay.append(winnerText);
    }



    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(StringBuilder sb, char aChar, int times)
    {
        for (int i = 0; i < times; i++) {
            sb.append(aChar);
        }
    }

    public void setTrackShape(String shape) {
        track.setShape(shape);
        printRace();
    }
    
    public void setWeatherCondition(String weather) {
        track.setWeatherCondition(weather);
        printRace();
    }

    public Track getTrack() {
        return track;
    }

    public List<HorseGUI> getHorses() {
        return horses;
    }

    public BettingSystem getBettingSystem() {
        return bettingSystem;
    }

    public void setLaneCount(int count) {
        if (count >= 2 && count <= 25) {
            this.laneCount = count;
            // Update track with new lane count
            track = new Track(trackLength, laneCount);
            
            // Create a temporary map to store existing horses
            HashMap<Integer, Horse> tempMap = new HashMap<>();
            
            // Copy existing horses to temporary map
            for (Map.Entry<Integer, Horse> entry : horseMap.entrySet()) {
                if (entry.getKey() < count) {  // Only keep horses that fit in new lane count
                    tempMap.put(entry.getKey(), entry.getValue());
                }
            }
            
            // Clear and update the main maps
            horseMap.clear();
            horses.clear();
            
            // Restore existing horses
            for (Map.Entry<Integer, Horse> entry : tempMap.entrySet()) {
                horseMap.put(entry.getKey(), entry.getValue());
                if (entry.getValue() instanceof HorseGUI) {
                    horses.add((HorseGUI) entry.getValue());
                }
            }
            
            // Add new horses only if needed
            for (int i = horseMap.size(); i < count; i++) {
                // Generate random stats within reasonable limits
                double confidence = 0.5 + (Math.random() * 0.4); // Random between 0.5 and 0.9
                double speed = 0.6 + (Math.random() * 0.3);     // Random between 0.6 and 0.9
                double stamina = 0.5 + (Math.random() * 0.4);   // Random between 0.5 and 0.9
                
                HorseGUI horse = new HorseGUI((char)('@' + i), "Horse " + (i + 1), confidence);
                horse.setSpeed(speed);
                horse.setStamina(stamina);
                
                // Randomly assign breed and coat color
                String[] breeds = {"Thoroughbred", "Arabian", "Quarter Horse", "Appaloosa", "Paint"};
                String[] colors = {"Brown", "Black", "Grey", "White", "Chestnut"};
                horse.setBreed(breeds[(int)(Math.random() * breeds.length)]);
                horse.setCoatColor(colors[(int)(Math.random() * colors.length)]);
                
                // Randomly assign equipment
                String[] saddles = {"Standard", "Racing", "Lightweight", "Heavy"};
                String[] horseshoes = {"Standard", "Lightweight", "Heavy", "Racing"};
                horse.setSaddle(saddles[(int)(Math.random() * saddles.length)]);
                horse.setHorseshoes(horseshoes[(int)(Math.random() * horseshoes.length)]);
                
                horseMap.put(i, horse);
                horses.add(horse);
            }
            
            // Ensure all horses are in the horses list
            for (Horse horse : horseMap.values()) {
                if (horse != null && horse instanceof HorseGUI && !horses.contains(horse)) {
                    horses.add((HorseGUI) horse);
                }
            }
            
            printRace();
        }
    }
    
    public void setTrackLength(int length) {
        if (length >= 20 && length <= 200) {
            this.trackLength = length;
            this.raceLength = length;
            // Update track with new length
            track = new Track(trackLength, laneCount);
            printRace();
        }
    }
    
    public double getBestTimeForTrack(String shape) {
        return bestTimes.getOrDefault(shape + "_" + track.getWeatherCondition(), 0.0);
    }
    
    public double getAverageTimeForShape(String shape) {
        // This is a simplified implementation
        // In a real application, you'd track all times and calculate the average
        return getBestTimeForTrack(shape) * 1.2;
    }
    
    public int getTotalBets() {
        return totalBets;
    }
    
    public double getTotalBetAmount() {
        return totalBetAmount;
    }
    
    public int getWinningBets() {
        return winningBets;
    }
    
    public double getBettingWinRate() {
        return totalBets > 0 ? (double) winningBets / totalBets : 0.0;
    }
    
    public double getAverageBetAmount() {
        return totalBets > 0 ? totalBetAmount / totalBets : 0.0;
    }
    
    public List<String> getRecentBets() {
        return new ArrayList<>(recentBets);
    }
    
    public void recordBet(double amount, boolean won) {
        totalBets++;
        totalBetAmount += amount;
        if (won) winningBets++;
        
        String betRecord = String.format("$%.2f - %s", amount, won ? "Won" : "Lost");
        recentBets.add(betRecord);
        if (recentBets.size() > 10) {
            recentBets.remove(0);
        }
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setBettingSystem(BettingSystem bettingSystem) {
        this.bettingSystem = bettingSystem;
    }

    public boolean isRaceRunning() {
        return raceRunning;
    }

    public void addRaceEndListener(Runnable listener) {
        this.raceEndListener = listener;
    }
}

