#!/usr/bin/env bash
# Compiles the Break Out game with Java SDK 27.
set -e

classes_dir="out/classes"
mkdir -p "$classes_dir"

javac --release 27 -d "$classes_dir" src/hamzahqasim/BreakOut/*.java

echo "Build succeeded: $classes_dir"
echo "Run it with: ./run.sh"
