#!/usr/bin/env bash
echo "Starting the scala-microservice on port 9000 ..."
docker run -d --name scala-microservice -p 9000:9000 thoughtworks/service:scala-microservice-0.1
echo "... done."
