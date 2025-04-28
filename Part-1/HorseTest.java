public class HorseTest {
    public static void main(String[] args) {
        System.out.println("Testing Horse Class Functionality");
        System.out.println("--------------------------------");
        
        // Test 1: Create a new horse and verify initial state
        System.out.println("\nTest 1: Initial State Verification");
        Horse testHorse = new Horse('H', "Thunder", 0.7);
        System.out.println("Created horse: " + testHorse.getName());
        System.out.println("Initial confidence: " + testHorse.getConfidence());
        System.out.println("Initial distance: " + testHorse.getDistanceTravelled());
        System.out.println("Has fallen: " + testHorse.hasFallen());
        System.out.println("Is winner: " + testHorse.getIsWinner());
        
        // Test 2: Test movement functionality
        System.out.println("\nTest 2: Movement Testing");
        System.out.println("Moving horse forward 3 times...");
        testHorse.moveForward();
        testHorse.moveForward();
        testHorse.moveForward();
        System.out.println("Distance after moving: " + testHorse.getDistanceTravelled());
        
        // Test 3: Test falling functionality
        System.out.println("\nTest 3: Falling Testing");
        System.out.println("Making horse fall...");
        testHorse.fall();
        System.out.println("Has fallen: " + testHorse.hasFallen());
        
        // Test 4: Test reset functionality
        System.out.println("\nTest 4: Reset Testing");
        System.out.println("Resetting horse to start...");
        testHorse.goBackToStart();
        System.out.println("Distance after reset: " + testHorse.getDistanceTravelled());
        System.out.println("Has fallen after reset: " + testHorse.hasFallen());
        
        // Test 5: Test confidence modification and bounds checking
        System.out.println("\nTest 5: Confidence Bounds Testing");
        System.out.println("Testing normal confidence value (0.9)...");
        testHorse.setConfidence(0.9);
        System.out.println("New confidence: " + testHorse.getConfidence());
        
        System.out.println("\nTesting confidence above bounds (1.5)...");
        testHorse.setConfidence(1.5);
        System.out.println("Confidence after setting above bounds: " + testHorse.getConfidence());
        
        System.out.println("\nTesting confidence below bounds (-0.5)...");
        testHorse.setConfidence(-0.5);
        System.out.println("Confidence after setting below bounds: " + testHorse.getConfidence());
        
        // Test 6: Test winner status
        System.out.println("\nTest 6: Winner Status Testing");
        System.out.println("Setting horse as winner...");
        testHorse.setIsWinner(true);
        System.out.println("Is winner: " + testHorse.getIsWinner());
        
        // Test 7: Test symbol modification
        System.out.println("\nTest 7: Symbol Testing");
        System.out.println("Current symbol: " + testHorse.getSymbol());
        System.out.println("Changing symbol...");
        testHorse.setSymbol('T');
        System.out.println("New symbol: " + testHorse.getSymbol());
        
        System.out.println("\nAll tests completed!");
    }
} 