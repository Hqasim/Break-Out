# Runs the Break Out game. Builds first if it hasn't been built yet.
$ErrorActionPreference = "Stop"

$classesDir = "out/classes"
if (-not (Test-Path $classesDir)) {
    & ./build.ps1
}

java -cp $classesDir hamzahqasim.BreakOut.GameApp
