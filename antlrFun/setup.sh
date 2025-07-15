#!/usr/bin/bash

antlr4='java -jar /usr/local/lib/antlr-4.13.2-complete.jar'
$antlr4 Expr.g4
javac *.java
