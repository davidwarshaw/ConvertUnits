#! /bin/sh

# Build docker image
docker build -t convertunits:1.0-snapshot .
# Run docker container
docker run -p 8080:8080 convertunits:1.0-snapshot
