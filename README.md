# HorseRaceSimulator

A Java-based horse racing simulator featuring text-based race visualization, multiple horses with unique characteristics, and dynamic race mechanics based on confidence levels. Demonstrates OOP principles with encapsulation and real-time console animation.

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
- [Development](#development)
- [Contributing](#contributing)
- 
## Project Overview
This project implements a horse racing simulation that demonstrates core Object-Oriented Programming principles. The simulation features dynamic race mechanics where each horse's performance is influenced by its confidence level, creating an engaging and unpredictable racing experience.

## Features
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

- **Race Mechanics**
  - Configurable race length
  - Multiple horse support
  - Dynamic movement probabilities
  - Winner determination

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

2. The race will start automatically with the default configuration:
   - Race length: 20 units
   - 3 horses with varying confidence levels
   - Real-time race visualization

### Customizing the Race
To modify the race parameters, edit the `Main.java` file:
```java
Race race = new Race(20); // Change race length
race.addHorse(new Horse('H', "Horse 1", 0.5), 1); // Add/modify horses
```

## Project Structure
HorseRaceSimulator/
├── Horse.java # Horse class implementation
├── Race.java # Race class implementation
├── Main.java # Main program entry point
└── README.md # This documentation


## Dependencies
- Java Standard Library
- No external dependencies required

## Development

### Code Organization
- `Horse.java`: Implements horse characteristics and behavior
- `Race.java`: Manages race mechanics and visualization
- `Main.java`: Program entry point and race initialization

### Key Classes
- **Horse Class**
  - Encapsulates horse attributes
  - Manages movement and fall mechanics
  - Handles confidence-based performance

- **Race Class**
  - Manages race progression
  - Handles horse interactions
  - Provides race visualization

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Acknowledgments
- Original starter code by Pony McRaceface
- Java documentation and community resources
