# Compiles the Break Out game with Java SDK 27.
$ErrorActionPreference = "Stop"

$classesDir = "out/classes"
New-Item -ItemType Directory -Force -Path $classesDir | Out-Null

javac --release 27 -d $classesDir src/hamzahqasim/BreakOut/*.java
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Output "Build succeeded: $classesDir"
Write-Output "Run it with: ./run.ps1"
