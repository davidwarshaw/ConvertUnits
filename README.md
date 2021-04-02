## ConvertUnits

#### David Warshaw

To build and run in a docker container:

    ./build_and_run.sh

Which is just:

    # Build docker image
    docker build -t convertunits:1.0-snapshot .
    
    # Run docker container
    docker run -p 8080:8080 convertunits:1.0-snapshot

The service is up and ready to respond to requests on `http://locahost:8080` when you see this line:

    ...
    ... org.eclipse.jetty.server.Server: Started ...
    ...
