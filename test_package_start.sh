#!/bin/sh

# Build app
mvn package

# Run tests
mvn test

# Run app
java -jar target/ConvertUnits-1.0-SNAPSHOT.jar server
