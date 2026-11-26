# DUAL RACER — 2-Player Local Traffic Racing Game

A fast-paced 2D arcade traffic racing game built in Java with Swing and Java2D Graphics, featuring an independent vertical split-screen experience for two players on a single computer.

## 🏁 Overview

- **Platform:** Windows Desktop
- **Language:** Java 21+
- **Build System:** Maven
- **Graphics:** Java Swing + Java2D (Hardware-accelerated Antialiased Graphics2D)
- **Target Resolution:** 1280 × 720 @ 60 FPS
- **Multiplayer:** 100% Local 2-Player simultaneous split-screen (No networking or internet required)

---

## 🎮 Player Controls

Both players control their vehicle simultaneously without key-ghosting or input locking:

| Action | Player 1 (Red Car - Left Road) | Player 2 (Blue Car - Right Road) |
| :--- | :--- | :--- |
| **Accelerate** | `W` | `Up Arrow` (`↑`) |
| **Brake / Reverse** | `S` | `Down Arrow` (`↓`) |
| **Steer Left** | `A` | `Left Arrow` (`←`) |
| **Steer Right** | `D` | `Right Arrow` (`→`) |
| **Nitro Boost** | `SPACE` | `Right Shift` |

- **Pause Game:** `ESC`

---

## 🚀 How to Run

### Option 1: Quick Batch Script (Windows)
Double-click or run from command prompt:
```bat
run.bat
```

### Option 2: Maven Command Line
```bash
mvn clean compile exec:java
```

### Option 3: Run Unit Tests
```bash
mvn test
```

---

## 🏗️ Architecture & Packages

```
com.dualracer
 ├── Main.java                 # Window setup and launcher
 ├── core/
 │    ├── Game.java            # Main coordinator and game state
 │    ├── GameConfig.java      # Settings, difficulty, durations
 │    ├── GameLoop.java        # 60 FPS delta-time physics loop
 │    └── GameState.java       # Finite state machine
 ├── entities/
 │    ├── Car.java             # Base vehicle physics model
 │    ├── Player.java          # Player state, nitro, tilt, lives
 │    ├── TrafficCar.java      # Autonomous traffic vehicle entity
 │    └── VehicleType.java     # Sedan, Sports, SUV, Truck, Taxi specs
 ├── graphics/
 │    ├── GamePanel.java       # Double-buffered Swing display
 │    ├── Renderer.java        # Antialiased car, lighting, nitro visuals
 │    └── RoadRenderer.java    # Dynamic asphalt, lanes, rumble curbs
 ├── managers/
 │    ├── InputManager.java    # Swing WHEN_IN_FOCUSED_WINDOW key bindings
 │    └── TrafficManager.java  # Independent lane spawning & pattern control
 ├── ui/
 │    └── HUD.java             # Dual meters, speed, hearts, timer
 └── utils/
      ├── Constants.java       # Central dimensions, colors, physics rates
      └── MathUtils.java       # Math helpers (clamp, lerp)
```
