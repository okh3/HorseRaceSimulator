# HorseRaceSimulator

A Java-based horse racing simulator featuring both text-based and GUI race visualization, multiple horses with unique characteristics, dynamic race mechanics based on confidence levels, and a comprehensive betting system. Demonstrates OOP principles with encapsulation and real-time animation.

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
- [Development](#development)
- [Contributing](#contributing)

## Project Overview
This project implements a horse racing simulation that demonstrates core Object-Oriented Programming principles. The simulation features dynamic race mechanics where each horse's performance is influenced by its confidence level, track conditions, weather effects, and equipment choices, creating an engaging and unpredictable racing experience.

## Features

### Core Racing Features
- **Text-based and GUI Race Visualization**
  - Real-time race progress display
  - Clear lane visualization
  - Dynamic horse movement
  - Fallen horse indicators

- **Horse Characteristics**
  - Unique names and symbols
  - Configurable confidence levels
  - Dynamic performance based on confidence
  - Fall mechanics
  - Multiple horse breeds (Thoroughbred, Arabian, Quarter Horse, etc.)
  - Customizable equipment (saddles, horseshoes)

### Phase 2 Enhancements
- **Track Management**
  - Multiple track shapes (Oval, Figure-eight, Straight, Zigzag)
  - Dynamic weather conditions (Clear, Rainy, Snowy)
  - Weather effects on horse performance
  - Track length and lane count configuration

- **Betting System**
  - Real-time odds calculation
  - Multiple betting options
  - Betting statistics and history
  - Player balance management
  - Betting feedback and recommendations

- **Advanced Statistics**
  - Horse performance tracking
  - Race history and records
  - Track-specific statistics
  - Weather impact analysis
  - Betting success rates

- **GUI Interface**
  - Modern Swing-based interface
  - Real-time race visualization
  - Interactive controls
  - Statistics dashboard
  - Betting interface

## Installation

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Git (for version control)

### Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/HorseRaceSimulator.git
   ```

2. Navigate to the project directory:
   ```bash
   cd HorseRaceSimulator
   ```

3. Compile the Java files:
   ```bash
   javac *.java
   ```

## Usage

### Running the Simulation
1. After compilation, run the main class:
   ```bash
   java Main
   ```

2. The race will start with the default configuration:
   - Race length: 20 units
   - 3 horses with varying confidence levels
   - Real-time race visualization
   - Interactive GUI controls

### Customizing the Race
The GUI interface allows you to:
- Configure track shape and length
- Set weather conditions
- Add and customize horses
- Place bets
- View statistics
- Adjust race parameters

## Project Structure
HorseRaceSimulator/
├── Horse.java # Base horse class implementation
├── HorseGUI.java # Enhanced horse class with GUI support
├── Race.java # Race class implementation
├── Track.java # Track management and conditions
├── BettingSystem.java # Betting system implementation
├── Statistics.java # Statistics tracking
├── Main.java # Program entry point
├── MainGUI.java # GUI implementation
└── README.md # This documentation

## Dependencies
- Java Standard Library
- Java Swing (GUI components)
- No external dependencies required

## Development

### Code Organization
- `Horse.java`: Base horse characteristics and behavior
- `HorseGUI.java`: Enhanced horse class with GUI support
- `Race.java`: Manages race mechanics and visualization
- `Track.java`: Handles track conditions and weather effects
- `BettingSystem.java`: Manages betting mechanics and odds
- `Statistics.java`: Tracks race and betting statistics
- `MainGUI.java`: GUI implementation and user interface
- `Main.java`: Program entry point

### Key Classes
- **Horse Classes**
  - Encapsulates horse attributes
  - Manages movement and fall mechanics
  - Handles confidence-based performance
  - Supports multiple breeds and equipment

- **Race Class**
  - Manages race progression
  - Handles horse interactions
  - Provides race visualization
  - Integrates with betting system

- **Track Class**
  - Manages track conditions
  - Handles weather effects
  - Provides track statistics
  - Supports multiple track shapes

- **BettingSystem Class**
  - Manages betting mechanics
  - Calculates odds
  - Tracks betting history
  - Provides betting recommendations

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Acknowledgments
- Original starter code by Pony McRaceface
- Java documentation and community resources
- Swing GUI framework

