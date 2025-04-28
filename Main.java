public class Main {

    public static void main(String[] args) {
        // Set console to UTF-8 encoding
        try {
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("console.encoding", "UTF-8");
        } catch (Exception e) {
            // Ignore if we can't set the encoding
        }
        
        Race race = new Race(20);
        
        // Add three horses to the race
        race.addHorse(new Horse('H', "Horse 1", 0.5), 1);
        race.addHorse(new Horse('S', "Horse 2", 0.7), 2);
        race.addHorse(new Horse('C', "Horse 3", 0.4), 3);
        
        race.startRace();
    }
}
 