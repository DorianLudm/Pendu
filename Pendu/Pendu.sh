#!/bin/bash
javac -d bin --module-path /home/dorian/Desktop/javafx-sdk-17.0.7/lib/ --add-modules javafx.controls src/*.java
java -cp bin --module-path /home/dorian/Desktop/javafx-sdk-17.0.7/lib/ --add-modules javafx.controls Pendu