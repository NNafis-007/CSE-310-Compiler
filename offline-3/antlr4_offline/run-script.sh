#!/usr/bin/bash

# Check for at least one argument
if [ $# -lt 1 ]; then
    echo "Usage: $0 <input_file>"
    exit 1
fi

ANTLR_JAR="/usr/local/lib/antlr-4.13.2-complete.jar"

java -Xmx500M -cp "$ANTLR_JAR:$CLASSPATH" org.antlr.v4.Tool C8086Lexer.g4 C8086Parser.g4
javac -cp ".:$ANTLR_JAR" C8086*.java Main.java
java -cp ".:$ANTLR_JAR" Main "$1"
