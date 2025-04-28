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

### Part 1: Core Racing Features
- **Text-based Race Visualization**
  - Real-time race progress display
  - Clear lane visualization
  - Dynamic horse movement
  - Fallen horse indicators

- **Horse Characteristics**
  - Unique names and symbols
  - Configurable confidence levels
  - Dynamic performance based on confidence
  - Fall mechanics

### Part 2: Enhanced Features
- **GUI Race Visualization**
  - Modern Swing-based interface
  - Real-time race visualization
  - Interactive controls
  - Statistics dashboard

- **Advanced Horse Management**
  - Multiple horse breeds (Thoroughbred, Arabian, Quarter Horse, etc.)
  - Customizable equipment (saddles, horseshoes)
  - Enhanced performance tracking

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

## Installation

### Prerequisites
- Java Development Kit (JDK) 11 or higher
  - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
  - Verify installation by running `java -version` in your terminal
- Git (for version control)
  - Download from: [Git Downloads](https://git-scm.com/downloads)

### Detailed Setup Instructions
1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/HorseRaceSimulator.git
   ```

2. **Navigate to the project directory**:
   ```bash
   cd HorseRaceSimulator
   ```

3. **Compile the Java files**:
   - For Windows/macOS/Linux (Part 1):
     ```bash
     javac -encoding UTF-8 Part-1/*.java
     ```

   - For Windows/macOS/Linux (Part 2):
     ```bash
     javac -encoding UTF-8 Part-2/*.java
     ```

## Usage

### Running the Simulation
1. **Start the race simulation**:
   - For Part 1 (Text-based):
     ```bash
     java -Dfile.encoding=UTF-8 -cp Part-1 Main
     ```
   - For Part 2 (GUI-based):
     ```bash
     java -Dfile.encoding=UTF-8 -cp Part-2 Main
     ```

2. **Understanding the Output**:
   - Part 1: Text-based display with real-time race progress
   - Part 2: Interactive GUI with enhanced visualization and controls

### Customizing the Race
The program supports different configurations in each part:

**Part 1:**
- Race length (default: 20 units)
- Number of horses (default: 3)
- Horse confidence levels
- Basic race visualization settings

**Part 2:**
- Advanced track configurations
- Weather conditions
- Horse breeds and equipment
- Betting system settings
- Statistics tracking

## Project Structure
```
HorseRaceSimulator/
├── Part-1/
│   ├── Horse.java        # Base horse class implementation
│   ├── Race.java         # Race class implementation
│   ├── Main.java         # Program entry point
│   └── README.md         # Documentation
├── Part-2/
│   ├── HorseGUI.java     # Enhanced horse class with GUI support
│   ├── RaceGUI.java      # GUI-based race implementation
│   ├── Track.java        # Track management and conditions
│   ├── BettingSystem.java # Betting system implementation
│   ├── Statistics.java   # Statistics tracking
│   ├── Main.java         # GUI program entry point
│   └── README.md         # Documentation
```

## Dependencies
- Java Standard Library
- Java Swing (for Part 2 GUI components)
- No external dependencies required

## Development

### Code Organization
**Part 1:**
- `Horse.java`: Base horse characteristics and behavior
- `Race.java`: Manages race mechanics and visualization
- `Main.java`: Program entry point

**Part 2:**
- `HorseGUI.java`: Enhanced horse class with GUI support
- `RaceGUI.java`: GUI-based race management
- `Track.java`: Handles track conditions and weather effects
- `BettingSystem.java`: Manages betting mechanics and odds
- `Statistics.java`: Tracks race and betting statistics
- `Main.java`: GUI program entry point

### Key Classes
**Part 1:**
- **Horse Class**
  - Encapsulates horse attributes
  - Manages movement and fall mechanics
  - Handles confidence-based performance

- **Race Class**
  - Manages race progression
  - Handles horse interactions
  - Provides text-based visualization

**Part 2:**
- **HorseGUI Class**
  - Enhanced horse characteristics
  - GUI visualization support
  - Advanced performance tracking

- **RaceGUI Class**
  - GUI-based race management
  - Interactive controls
  - Enhanced visualization

- **Track Class**
  - Manages track conditions
  - Handles weather effects
  - Provides track statistics

- **BettingSystem Class**
  - Manages betting mechanics
  - Calculates odds
  - Tracks betting history

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Troubleshooting
If you encounter any issues:

1. **Encoding Problems**:
   - Make sure to use the `-Dfile.encoding=UTF-8` flag when running the program
   - Set your terminal/console to use UTF-8 encoding

2. **Compilation Errors**:
   - Verify your JDK installation
   - Ensure you're in the correct directory
   - Check for any syntax errors in the code

3. **Runtime Errors**:
   - Make sure all class files are compiled
   - Verify the classpath is set correctly
   - Check for any missing dependencies

4. **GUI Issues (Part 2)**:
   - Ensure Java Swing is properly installed
   - Check display settings and resolution
   - Verify all GUI components are properly initialized

## Acknowledgments
- Original starter code by Pony McRaceface
- Java documentation and community resources
- Swing GUI framework
