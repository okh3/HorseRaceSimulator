import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;
import java.util.List;

public class MainGUI extends JFrame {
    private Race race;
    private JTextArea raceDisplay;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JComboBox<String> trackShapeCombo;
    private JComboBox<String> weatherCombo;
    private JTextField betAmount;
    private JButton placeBetButton;
    private JLabel balanceLabel;
    private JComboBox<HorseGUI> horseCombo;
    private JTextArea currentBetsArea;
    private JSpinner laneSpinner;
    private JSpinner lengthSpinner;
    private JComboBox<String> shapeCombo;
    
    // Add missing field declarations
    private JButton applyButton;
    private JComboBox<HorseGUI> horseSelectCombo;
    private JTextField nameField;
    private JComboBox<String> breedCombo;
    private JComboBox<String> colorCombo;
    private JComboBox<String> symbolCombo;
    private JComboBox<String> saddleCombo;
    private JComboBox<String> shoesCombo;
    private JButton horseApplyButton;
    
    private JTextArea bettingInfoArea;
    private JTextArea statisticsArea;
    private JTextArea trackInfoArea;
    
    public MainGUI() {
        setTitle("Horse Race Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 1000));
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Initialize race first
        raceDisplay = new JTextArea();
        raceDisplay.setEditable(false);
        raceDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        race = new Race(50, raceDisplay);
        
        // Initialize betting system with race
        race.setBettingSystem(new BettingSystem(race.getStatistics(), race));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane raceScrollPane = new JScrollPane(raceDisplay);
        raceScrollPane.setPreferredSize(new Dimension(600, 400));
        mainPanel.add(raceScrollPane, BorderLayout.CENTER);
        
        // Create control panels
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setPreferredSize(new Dimension(800, 350));
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(250, 600));
        
        // Add race controls to bottom panel
        JPanel raceControls = new JPanel();
        raceControls.setLayout(new GridLayout(1, 3));
        raceControls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        startButton = new JButton("Start Race");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");
        raceControls.add(startButton);
        raceControls.add(pauseButton);
        raceControls.add(resetButton);
        bottomPanel.add(raceControls);
        
        // Add betting panel to bottom panel
        JPanel bettingPanel = createBettingPanel();
        bettingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        bottomPanel.add(bettingPanel);
        
        // Add customization and statistics panels to right panel
        JPanel trackPanel = createTrackCustomizationPanel();
        trackPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        rightPanel.add(trackPanel);
        rightPanel.add(Box.createVerticalStrut(5));
        
        JPanel horsePanel = createHorseCustomizationPanel();
        horsePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        rightPanel.add(horsePanel);
        rightPanel.add(Box.createVerticalStrut(5));
        
        JPanel statsPanel = createStatisticsPanel();
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        rightPanel.add(statsPanel);
        
        // Add panels to main panel
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("Show Help");
        helpItem.addActionListener(e -> showHelpDialog());
        helpMenu.add(helpItem);
        menuBar.add(helpMenu);
        
        // Add Help button to menu bar
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(e -> showHelpDialog());
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpButton);
        
        // Add Bet Info button next to Help button
        JButton bettingInfoButton = new JButton("Bet Info");
        bettingInfoButton.addActionListener(e -> showBettingInfoDialog());
        menuBar.add(bettingInfoButton);
        
        setJMenuBar(menuBar);
        
        add(mainPanel);
        
        // Add action listeners for race controls
        startButton.addActionListener(e -> {
            race.startRace();
            enableCustomizationControls(false);  // Disable customization when race starts
            horseCombo.setEnabled(false);
            betAmount.setEnabled(false);
            placeBetButton.setEnabled(false);
        });
        
        pauseButton.addActionListener(e -> {
            if (race.isRaceRunning()) {
                if (pauseButton.getText().equals("Pause")) {
                    race.stopRace();
                    pauseButton.setText("Continue");
                    enableCustomizationControls(true);   // Re-enable customization when race is paused
                    horseCombo.setEnabled(true);
                    betAmount.setEnabled(true);
                    placeBetButton.setEnabled(true);
                } else {
                    race.startRace();
                    pauseButton.setText("Pause");
                    enableCustomizationControls(false);  // Disable customization when race continues
                    horseCombo.setEnabled(false);
                    betAmount.setEnabled(false);
                    placeBetButton.setEnabled(false);
                }
            }
        });
        
        resetButton.addActionListener(e -> {
            race.resetRace();
            enableCustomizationControls(true);   // Re-enable customization when race is reset
            updateHorseCombo();
            updateBalanceLabel();
            updateCurrentBets();
            enableBettingControls();
        });

        // Add race end listener
        race.addRaceEndListener(() -> {
            enableCustomizationControls(true);
            horseCombo.setEnabled(true);
            betAmount.setEnabled(true);
            placeBetButton.setEnabled(true);
        });
        
        // Initial display
        race.printRace();
    }
    
    private void updateCurrentBets() {
        StringBuilder betsText = new StringBuilder();
        Map<HorseGUI, Double> currentBets = race.getBettingSystem().getBets();
        boolean hasBets = false;
        
        for (Map.Entry<HorseGUI, Double> entry : currentBets.entrySet()) {
            if (entry.getValue() > 0) {
                betsText.append(String.format("%s: $%.2f at %.2f:1 odds\n", 
                    entry.getKey().getName(),
                    entry.getValue(),
                    race.getBettingSystem().getOdds(entry.getKey())));
                hasBets = true;
            }
        }
        
        if (!hasBets) {
            betsText.append("No bets placed yet");
        }
        
        currentBetsArea.setText(betsText.toString());
    }
    
    private void updateHorseCombo() {
        // Update the betting panel's horse combo
        if (horseCombo != null) {
            HorseGUI selectedHorse = (HorseGUI) horseCombo.getSelectedItem();
            horseCombo.removeAllItems();
            for (HorseGUI horse : race.getHorses()) {
                if (horse != null) {
                    horseCombo.addItem(horse);
                }
            }
            if (selectedHorse != null && race.getHorses().contains(selectedHorse)) {
                horseCombo.setSelectedItem(selectedHorse);
            } else if (horseCombo.getItemCount() > 0) {
                horseCombo.setSelectedIndex(0);
            }
        }

        // Update the horse customization panel's horse combo
        if (horseSelectCombo != null) {
            HorseGUI selectedHorse = (HorseGUI) horseSelectCombo.getSelectedItem();
            horseSelectCombo.removeAllItems();
            for (HorseGUI horse : race.getHorses()) {
                if (horse != null) {
                    horseSelectCombo.addItem(horse);
                }
            }
            if (selectedHorse != null && race.getHorses().contains(selectedHorse)) {
                horseSelectCombo.setSelectedItem(selectedHorse);
            } else if (horseSelectCombo.getItemCount() > 0) {
                horseSelectCombo.setSelectedIndex(0);
            }
        }
    }
    
    public void updateBalanceLabel() {
        balanceLabel.setText("Balance: $" + String.format("%.2f", race.getBettingSystem().getPlayerBalance()));
    }
    
    private void showHelpDialog() {
        String helpText = 
            "Horse Race Simulator Help\n\n" +
            "Race Controls:\n" +
            "- Start Race: Begins the race simulation\n" +
            "- Pause: Temporarily stops the race\n" +
            "- Reset: Returns all horses to starting position\n\n" +
            "Track Settings:\n" +
            "- Track Shape:\n" +
            "  • Oval: Standard racing conditions (100% speed)\n" +
            "  • Figure-eight: Slightly slower (90% speed) due to turns\n" +
            "  • Custom: Slowest (80% speed) due to complex layout\n\n" +
            "- Weather Conditions:\n" +
            "  • Clear: Normal racing conditions\n" +
            "  • Rainy: Horses move 20% slower, 50% higher chance of falling\n" +
            "  • Snowy: Horses move 40% slower, 100% higher chance of falling\n\n" +
            "Betting System:\n" +
            "- Select a horse from the dropdown\n" +
            "- Enter your bet amount\n" +
            "- Click 'Place Bet' to place your bet\n" +
            "- Winnings are calculated based on horse odds\n\n" +
            "Race Display:\n" +
            "- Shows current race progress\n" +
            "- Displays horse positions and confidence levels\n" +
            "- Shows race results when race ends\n" +
            "- '+' symbol indicates a fallen horse\n\n" +
            "File Menu:\n" +
            "- New Race: Starts a fresh race\n" +
            "- Exit: Closes the application";

        JOptionPane.showMessageDialog(this, helpText, "Help Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showBettingInfoDialog() {
        String bettingInfo = 
            "How Betting Works:\n\n" +
            "1. Minimum Bet: $2.00\n" +
            "2. Odds Format: X:1 (e.g., 3:1 means you win $3 for every $1 bet)\n" +
            "3. Payout Calculation: Bet Amount * Odds\n" +
            "   Example: $10 bet at 3:1 odds = $30 winnings\n" +
            "   You get your original bet back plus the winnings\n\n" +
            "Odds Calculation:\n" +
            "• Based on horse confidence (0.0 to 1.0)\n" +
            "• Modified by track shape and weather conditions\n" +
            "• Includes 15% house edge (standard in horse racing)\n\n" +
            "Special Cases:\n" +
            "• If all horses fall: All bets are refunded\n" +
            "• If race is reset: All bets are refunded\n" +
            "• If race is paused: Bets remain active\n\n" +
            "Track Effects:\n" +
            "• Oval: +10% confidence for all horses\n" +
            "• Straight: No modifier\n" +
            "• Curved: -10% confidence for all horses\n\n" +
            "Weather Effects:\n" +
            "• Clear: No modifier\n" +
            "• Rain: -20% confidence, +50% fall chance\n" +
            "• Snow: -30% confidence, +100% fall chance";

        JOptionPane.showMessageDialog(this, bettingInfo, "Betting Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void clearBettingUI() {
        betAmount.setText("");
        currentBetsArea.setText("No bets placed yet");
        updateCurrentBets();
    }
    
    public void enableBettingControls() {
        horseCombo.setEnabled(true);
        betAmount.setEnabled(true);
        placeBetButton.setEnabled(true);
    }
    
    private void enableCustomizationControls(boolean enable) {
        // Track customization controls
        laneSpinner.setEnabled(enable);
        lengthSpinner.setEnabled(enable);
        shapeCombo.setEnabled(enable);
        weatherCombo.setEnabled(enable);
        applyButton.setEnabled(enable);
        
        // Horse customization controls
        horseSelectCombo.setEnabled(enable);
        nameField.setEnabled(enable);
        breedCombo.setEnabled(enable);
        colorCombo.setEnabled(enable);
        symbolCombo.setEnabled(enable);
        saddleCombo.setEnabled(enable);
        shoesCombo.setEnabled(enable);
        horseApplyButton.setEnabled(enable);
    }
    
    private JPanel createTrackCustomizationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Track Customization"));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 2));

        optionsPanel.add(new JLabel("Number of Lanes:"));
        laneSpinner = new JSpinner(new SpinnerNumberModel(4, 2, 25, 1));
        optionsPanel.add(laneSpinner);

        optionsPanel.add(new JLabel("Track Length (meters):"));
        lengthSpinner = new JSpinner(new SpinnerNumberModel(50, 20, 200, 10));
        optionsPanel.add(lengthSpinner);

        optionsPanel.add(new JLabel("Track Shape:"));
        String[] shapes = {"Oval", "Figure-eight", "Straight", "Zigzag", "Custom"};
        shapeCombo = new JComboBox<>(shapes);
        optionsPanel.add(shapeCombo);

        optionsPanel.add(new JLabel("Weather:"));
        String[] weathers = {"Clear", "Rainy", "Snowy", "Foggy", "Windy"};
        weatherCombo = new JComboBox<>(weathers);
        optionsPanel.add(weatherCombo);

        applyButton = new JButton("Apply Changes");
        applyButton.addActionListener(e -> {
            int lanes = (int) laneSpinner.getValue();
            int length = (int) lengthSpinner.getValue();
            String shape = (String) shapeCombo.getSelectedItem();
            String weather = (String) weatherCombo.getSelectedItem();
            
            race.setLaneCount(lanes);
            race.setTrackLength(length);
            race.setTrackShape(shape);
            race.setWeatherCondition(weather);
            
            updateHorseCombo();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(applyButton);

        panel.add(optionsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel createHorseCustomizationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Horse Customization"));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Select Horse:"));
        horseSelectCombo = new JComboBox<>();
        updateHorseCombo(horseSelectCombo);
        selectionPanel.add(horseSelectCombo);
        panel.add(selectionPanel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(6, 2));

        optionsPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        if (horseSelectCombo.getItemCount() > 0) {
            HorseGUI firstHorse = (HorseGUI) horseSelectCombo.getItemAt(0);
            nameField.setText(firstHorse.getName());
        }
        optionsPanel.add(nameField);

        optionsPanel.add(new JLabel("Breed:"));
        String[] breeds = {"Thoroughbred", "Arabian", "Quarter Horse", "Appaloosa", "Paint"};
        breedCombo = new JComboBox<>(breeds);
        optionsPanel.add(breedCombo);

        optionsPanel.add(new JLabel("Coat Color:"));
        String[] colors = {"Brown", "Black", "Grey", "White", "Chestnut"};
        colorCombo = new JComboBox<>(colors);
        optionsPanel.add(colorCombo);

        optionsPanel.add(new JLabel("Symbol:"));
        String[] symbols = {"@", "#", "$", "%", "&", "*", "+", "=", "?", "!", "~", "^", ">", "<", "|"};
        symbolCombo = new JComboBox<>(symbols);
        optionsPanel.add(symbolCombo);

        optionsPanel.add(new JLabel("Saddle:"));
        String[] saddles = {"Standard", "Racing", "Lightweight", "Heavy"};
        saddleCombo = new JComboBox<>(saddles);
        optionsPanel.add(saddleCombo);

        optionsPanel.add(new JLabel("Horseshoes:"));
        String[] horseshoeTypes = {"Standard", "Lightweight", "Heavy", "Racing"};
        shoesCombo = new JComboBox<>(horseshoeTypes);
        optionsPanel.add(shoesCombo);

        horseApplyButton = new JButton("Apply Changes");
        horseApplyButton.addActionListener(e -> {
            HorseGUI selectedHorse = (HorseGUI) horseSelectCombo.getSelectedItem();
            if (selectedHorse != null) {
                selectedHorse.setName(nameField.getText());
                selectedHorse.setBreed((String) breedCombo.getSelectedItem());
                selectedHorse.setCoatColor((String) colorCombo.getSelectedItem());
                String selectedSymbol = (String) symbolCombo.getSelectedItem();
                if (selectedSymbol != null && !selectedSymbol.isEmpty()) {
                    selectedHorse.setSymbol(selectedSymbol.charAt(0));
                }
                selectedHorse.setSaddle((String) saddleCombo.getSelectedItem());
                selectedHorse.setHorseshoes((String) shoesCombo.getSelectedItem());
                race.printRace();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(horseApplyButton);

        panel.add(optionsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener to update fields when horse is selected
        horseSelectCombo.addActionListener(e -> {
            HorseGUI selectedHorse = (HorseGUI) horseSelectCombo.getSelectedItem();
            if (selectedHorse != null) {
                nameField.setText(selectedHorse.getName());
                breedCombo.setSelectedItem(selectedHorse.getBreed());
                colorCombo.setSelectedItem(selectedHorse.getCoatColor());
                symbolCombo.setSelectedItem(String.valueOf(selectedHorse.getSymbol()));
                saddleCombo.setSelectedItem(selectedHorse.getSaddle());
                shoesCombo.setSelectedItem(selectedHorse.getHorseshoes());
            }
        });

        return panel;
    }
    
    private void updateHorseCombo(JComboBox<HorseGUI> comboBox) {
        HorseGUI selectedHorse = (HorseGUI) comboBox.getSelectedItem();
        comboBox.removeAllItems();
        for (HorseGUI horse : race.getHorses()) {
            if (horse != null) {
                comboBox.addItem(horse);
            }
        }
        if (selectedHorse != null && race.getHorses().contains(selectedHorse)) {
            comboBox.setSelectedItem(selectedHorse);
        } else if (comboBox.getItemCount() > 0) {
            comboBox.setSelectedIndex(0);
        }
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Statistics & Analytics"));

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel performancePanel = new JPanel();
        performancePanel.setLayout(new BoxLayout(performancePanel, BoxLayout.Y_AXIS));
        JTextArea performanceText = new JTextArea();
        performanceText.setEditable(false);
        performanceText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane performanceScroll = new JScrollPane(performanceText);
        performancePanel.add(performanceScroll);
        tabbedPane.addTab("Performance", performancePanel);

        JPanel trackPanel = new JPanel();
        trackPanel.setLayout(new BoxLayout(trackPanel, BoxLayout.Y_AXIS));
        JTextArea trackText = new JTextArea();
        trackText.setEditable(false);
        trackText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane trackScroll = new JScrollPane(trackText);
        trackPanel.add(trackScroll);
        tabbedPane.addTab("Track Records", trackPanel);

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        JTextArea historyText = new JTextArea();
        historyText.setEditable(false);
        historyText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane historyScroll = new JScrollPane(historyText);
        historyPanel.add(historyScroll);
        tabbedPane.addTab("History", historyPanel);

        JPanel bettingPanel = new JPanel();
        bettingPanel.setLayout(new BoxLayout(bettingPanel, BoxLayout.Y_AXIS));
        JTextArea bettingText = new JTextArea();
        bettingText.setEditable(false);
        bettingText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane bettingScroll = new JScrollPane(bettingText);
        bettingPanel.add(bettingScroll);
        tabbedPane.addTab("Betting Analytics", bettingPanel);

        JButton updateButton = new JButton("Update Statistics");
        updateButton.addActionListener(e -> {
            updateStatistics(performanceText, trackText, historyText, bettingText);
        });

        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(updateButton, BorderLayout.SOUTH);

        return panel;
    }

    private void updateStatistics(JTextArea performanceText, JTextArea trackText, 
                                JTextArea historyText, JTextArea bettingText) {
        StringBuilder performance = new StringBuilder();
        for (HorseGUI horse : race.getHorses()) {
            performance.append(String.format("%s:\n", horse.getName()));
            performance.append(String.format("  Average Speed: %.2f m/s\n", horse.getAverageSpeed()));
            performance.append(String.format("  Win Ratio: %.1f%%\n", horse.getWinRatio() * 100));
            performance.append(String.format("  Confidence: %.2f\n", horse.getConfidence()));
            performance.append("\n");
        }
        performanceText.setText(performance.toString());

        StringBuilder track = new StringBuilder();
        track.append("Best Times:\n");
        for (String shape : new String[]{"Oval", "Figure-eight", "Straight", "Zigzag"}) {
            track.append(String.format("%s: %.2f seconds\n", shape, 
                race.getBestTimeForTrack(shape)));
        }
        trackText.setText(track.toString());

        StringBuilder history = new StringBuilder();
        for (HorseGUI horse : race.getHorses()) {
            history.append(String.format("%s's Race History:\n", horse.getName()));
            for (String race : horse.getRaceHistory()) {
                history.append("  ").append(race).append("\n");
            }
            history.append("\n");
        }
        historyText.setText(history.toString());

        StringBuilder betting = new StringBuilder();
        betting.append("Betting Statistics:\n");
        betting.append(String.format("Total Bets Placed: %d\n", race.getTotalBets()));
        betting.append(String.format("Win Rate: %.1f%%\n", race.getBettingWinRate() * 100));
        betting.append(String.format("Average Bet Amount: $%.2f\n", race.getAverageBetAmount()));
        betting.append("\nRecent Bets:\n");
        for (String bet : race.getRecentBets()) {
            betting.append("  ").append(bet).append("\n");
        }
        bettingText.setText(betting.toString());
    }
    
    private JPanel createBettingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Betting"));
        panel.setPreferredSize(new Dimension(800, 280));

        // Current bets panel
        JPanel currentBetsPanel = new JPanel(new BorderLayout());
        currentBetsPanel.add(new JLabel("Current Bets:"), BorderLayout.NORTH);
        currentBetsArea = new JTextArea(8, 20);
        currentBetsArea.setEditable(false);
        currentBetsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane betsScroll = new JScrollPane(currentBetsArea);
        currentBetsPanel.add(betsScroll, BorderLayout.CENTER);
        panel.add(currentBetsPanel, BorderLayout.NORTH);

        // Betting controls panel
        JPanel bettingControls = new JPanel();
        bettingControls.setLayout(new GridLayout(2, 3, 10, 10));
        
        // Balance and odds display
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        balanceLabel = new JLabel("Balance: $1000.00");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel oddsLabel = new JLabel("Current Odds: -");
        oddsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(balanceLabel);
        infoPanel.add(oddsLabel);
        bettingControls.add(infoPanel);
        
        // Horse selection with odds update
        horseCombo = new JComboBox<>();
        horseCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        updateHorseCombo(horseCombo);
        horseCombo.addActionListener(e -> {
            HorseGUI selectedHorse = (HorseGUI) horseCombo.getSelectedItem();
            if (selectedHorse != null) {
                double odds = race.getBettingSystem().getOdds(selectedHorse);
                oddsLabel.setText(String.format("Current Odds: %.2f:1", odds));
            }
        });
        bettingControls.add(horseCombo);
        
        // Bet amount input
        JPanel betAmountPanel = new JPanel(new BorderLayout());
        JLabel betLabel = new JLabel("Bet Amount:");
        betLabel.setFont(new Font("Arial", Font.BOLD, 14));
        betAmountPanel.add(betLabel, BorderLayout.WEST);
        betAmount = new JTextField();
        betAmount.setFont(new Font("Arial", Font.PLAIN, 14));
        betAmount.setColumns(10);
        betAmountPanel.add(betAmount, BorderLayout.CENTER);
        bettingControls.add(betAmountPanel);
        
        // Place bet button
        placeBetButton = new JButton("Place Bet");
        placeBetButton.setFont(new Font("Arial", Font.BOLD, 14));
        bettingControls.add(placeBetButton);
        
        // Add betting controls to panel
        panel.add(bettingControls, BorderLayout.CENTER);

        // Add action listeners
        placeBetButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(betAmount.getText());
                HorseGUI selectedHorse = (HorseGUI) horseCombo.getSelectedItem();
                if (selectedHorse != null) {
                    if (race.getBettingSystem().placeBet(selectedHorse, amount)) {
                        updateBalanceLabel();
                        updateCurrentBets();
                        JOptionPane.showMessageDialog(this, "Bet placed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to place bet. Check your balance and try again.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid bet amount");
            }
        });

        return panel;
    }
    
    private void updateAllHorseCombos() {
        // Update the horse selection combo in the horse customization panel
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                updateHorseComboInPanel((JPanel) comp);
            }
        }
        
        // Update the betting panel's horse combo
        if (horseCombo != null) {
            updateHorseCombo(horseCombo);
        }
    }

    private void updateHorseComboInPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) comp;
                if (combo.getItemCount() > 0 && combo.getItemAt(0) instanceof HorseGUI) {
                    updateHorseCombo((JComboBox<HorseGUI>) combo);
                }
            } else if (comp instanceof JPanel) {
                updateHorseComboInPanel((JPanel) comp);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
} 