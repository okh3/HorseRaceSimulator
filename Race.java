import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

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

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;

        // laneHorses were originally set to null which was causing a runtime exception

        horseMap  = new HashMap<>();

        // Using simple ASCII special characters that work in all terminals
        horseMap.put(0, new Horse('>', " Le Horse", 0.45));     // Arrow for Knight
        horseMap.put(1, new Horse('^', " Solider", 0.65));      // Arrow for Soldier
        horseMap.put(2, new Horse('#', " The Castle", 0.35));   // Hash for Castle
        horseMap.put(3, new Horse('*', " King", 0.75));         // Star for King
        horseMap.put(4, null);

        //        addHorse( new Horse('♞', " Le Horse", 0.2), 0); using addHorseMethod example
        //        addHorse( new Horse('♟', " Solider", 0.5), 1);
        //        addHorse( new Horse('♜', " The Castle", 0.4), 2);
        //        addHorse( new Horse('♛', " King", 0.6), 3);
        //        addHorse(null, 4);/

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
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        for(Horse horse: horseMap.values()){
            if(horse == null){
                continue;
            }
            horse.setIsWinner(false);
            horse.goBackToStart();
        }
        winnerHorse = null;

        //The logic of calling isAllHorsesFallen is there to stop the game when all horses have fallen, before there was an error
        // where if all fallen it would continue forever.

        while (!finished)
        {

            //move each horse
            for(Horse horse: horseMap.values()){
                if(horse == null){
                    continue;
                }
                moveHorse(horse);
            }
                        
            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            if ( raceWonBy() )
            {
                printWinner(winnerHorse);
                finished = true;
            }
            if(isAllHorsesFallen()){
                printNoWinner();
                finished = true;
            }
           
            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    //new introduced method to print all fallen horses and informing there is no winner.
    private void printNoWinner() {
        for(Horse horse: horseMap.values()){
            if(horse == null){
                continue;
            }
            System.out.println(horse.getSymbol() + " " + horse.getName() + " has fallen");

        }
        System.out.println("All Horses have fallen! no Winner!");
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
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        if  (!theHorse.hasFallen())
        {
            // New balanced movement logic:
            // Higher confidence means higher chance to move
            double moveChance = theHorse.getConfidence() * 0.9; // Increased from 0.8 to 0.9
            
            // Fall chance is now much lower and more balanced
            // Base fall chance of 0.02 (2%) modified by confidence
            double fallChance = 0.02 * (1.5 - theHorse.getConfidence());
            
            // First check if horse moves
            if (Math.random() < moveChance) {
                theHorse.moveForward();
            }
            
            // Then check if horse falls
            // The fall chance is now much lower and more realistic
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
    private void printRace()
    {
        // Clear the console and move cursor to top
        clearConsole();
        
        // Print race header
        System.out.println("HORSE RACE IN PROGRESS!");
        System.out.println("======================");
        System.out.println();

        // Print the track
        multiplePrint('=', raceLength + 3); //top edge of track
        System.out.println();

        // Print each horse's lane
        for (Horse horse : horseMap.values()) {
            printLane(horse);
            System.out.println();
        }

        multiplePrint('=', raceLength + 3); //bottom edge of track
        System.out.println();
        
        // Add a small delay to make the animation visible
        try {
            Thread.sleep(50); // 50ms delay for smoother animation
        } catch (InterruptedException e) {
            // Ignore interruption
        }
    }

    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        if (theHorse == null) {
            System.out.print('|');
            multiplePrint(' ', raceLength);
            System.out.print('|');
            System.out.println(" Empty");
            return;
        }

        // Calculate how many spaces are needed before and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        // Print a | for the beginning of the lane
        System.out.print('|');

        // Print the spaces before the horse
        multiplePrint(' ', spacesBefore);

        // If the horse has fallen, print fallen symbol; else print the horse's symbol
        if (theHorse.hasFallen())
        {
            System.out.print('+');  // Plus sign for fallen horses
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }

        // Print the spaces after the horse
        multiplePrint(' ', spacesAfter);

        // Print the | for the end of the track
        System.out.print('|');

        // Print the horse's name and confidence to the right of the lane
        System.out.printf(" %s (Current Confidence: %.1f)", theHorse.getName(), theHorse.getConfidence());
    }


    private void printWinner(Horse theHorse){
        if(theHorse == null) // added null check
            return;

        System.out.println("And the winner is................  " + winnerHorse.getName() + "!");
    }



    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

}
