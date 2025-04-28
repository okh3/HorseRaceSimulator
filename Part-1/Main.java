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
        race.startRace();
    }
}
 