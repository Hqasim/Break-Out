#!/usr/bin/env bash
# Runs the Break Out game. Builds first if it hasn't been built yet.
set -e

classes_dir="out/classes"
if [ ! -d "$classes_dir" ]; then
    ./build.sh
fi

java -cp "$classes_dir" hamzahqasim.BreakOut.GameApp
