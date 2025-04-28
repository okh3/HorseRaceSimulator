/**
 * Write a description of class Horse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse
    private char horseSymbol;
    private String horseName;
    private double horseConfidence;

    private int distanceTravelled;
    
    private boolean isFallen;
    private boolean isWinner;


    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
       this.horseSymbol = horseSymbol;
       this.horseName = horseName;
       if (horseConfidence < 0) {
           this.horseConfidence = 0;
       } else if (horseConfidence > 1) {
           this.horseConfidence = 1;
       } else {
           this.horseConfidence = horseConfidence;
       }
       this.isFallen = false;
       this.isWinner = false;
    }
    
    
    
    //Other methods of class Horse
    public void fall()
    {
        this.isFallen = true;
    }
    
    public double getConfidence()
    {
        return this.horseConfidence;
    }
    
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    public String getName()
    {
        return this.horseName;
    }
    
    public char getSymbol()
    {
        return this.horseSymbol;
    }

    public boolean getIsWinner(){
        return this.isWinner;
    }

    public void goBackToStart()
    {
        distanceTravelled = 0;
        isFallen = false;
    }

    
    public boolean hasFallen()
    {
        return this.isFallen;
    }

    public void moveForward()
    {
        distanceTravelled++;
    }

    public void setConfidence(double newConfidence)
    {
        if (newConfidence < 0) {
            this.horseConfidence = 0;
        } else if (newConfidence > 1) {
            this.horseConfidence = 1;
        } else {
            this.horseConfidence = newConfidence;
        }
    }
    
    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
    }

    public void setIsWinner(boolean value){
        this.isWinner = value;
    }

}
