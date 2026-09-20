# Break Out

This game application is inspired by the famous Breakout Arcade video game, developed and published by Atari, Inc., and released in 1976. Game is developed using Java programming language with Swing library.

![Break-Out](https://raw.githubusercontent.com/Hqasim/Break-Out/master/assets/GameScreenShot2.0.PNG?raw=true "Game Screen Shot")

## Requirements:
1. Install [JDK 27](https://jdk.java.net/27/) (or newer) for your Operating System, and make sure `java` and `javac` are on your `PATH`.

## How to run:
1. Clone the repository.
```bash
  git clone https://github.com/Hqasim/Break-Out.git
  cd Break-Out
```
2. Build and run from the terminal.

On Linux/macOS/Git Bash:
```bash
./build.sh
./run.sh
```

On Windows PowerShell:
```powershell
.\build.ps1
.\run.ps1
```

`run.sh` / `run.ps1` will build the project automatically if it hasn't been built yet.

## Built With

Java Swing and AWT library, targeting Java SDK 27.

## Versioning

Version 2.1:

- Fixed bug where pressing Enter did not start the game (keyboard focus was never given to the game panel)
- Upgraded to target Java SDK 27
- Removed JetBrains IDE project files and checked-in build artifacts in favor of a terminal-based build (`build.sh` / `build.ps1`)

Version 2.0:

- Added New Brick Colors
- Added different rebound angles of ball when it collides with paddle
- Added Starting game text
- Fixed new game issues
## Authors

* **Hamzah Qasim**

