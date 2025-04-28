import java.util.concurrent.TimeUnit;

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
    private Horse horse1;
    private Horse horse2;
    private Horse horse3;
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
        horse1 = null;
        horse2 = null;
        horse3 = null;
        winnerHorse = null;
    }

    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber == 1) {
            horse1 = theHorse;
        } else if (laneNumber == 2) {
            horse2 = theHorse;
        } else if (laneNumber == 3) {
            horse3 = theHorse;
        }
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        if (horse1 != null) {
            horse1.goBackToStart();
        }
        if (horse2 != null) {
            horse2.goBackToStart();
        }
        if (horse3 != null) {
            horse3.goBackToStart();
        }

        while (!finished)
        {
            //move each horse
            if (horse1 != null) {
                moveHorse(horse1);
            }
            if (horse2 != null) {
                moveHorse(horse2);
            }
            if (horse3 != null) {
                moveHorse(horse3);
            }
                        
            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            if (raceWonBy())
            {
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
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
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
        if (horse1 != null && horse1.getDistanceTravelled() == raceLength) {
            winnerHorse = horse1;
            return true;
        }
        if (horse2 != null && horse2.getDistanceTravelled() == raceLength) {
            winnerHorse = horse2;
            return true;
        }
        if (horse3 != null && horse3.getDistanceTravelled() == raceLength) {
            winnerHorse = horse3;
            return true;
        }
        return false;
    }

    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        printLane(horse1);
        System.out.println();
        printLane(horse2);
        System.out.println();
        printLane(horse3);
        System.out.println();

        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();
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

        // If the horse has fallen, print dead; else print the horse's symbol
        if (theHorse.hasFallen())
        {
            System.out.print('X');
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
        if(theHorse == null)
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
