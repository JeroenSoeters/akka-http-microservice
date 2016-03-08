#!/usr/bin/env bash
echo "Stopping the scala-microservice ..."
docker stop scala-microservice; docker rm scala-microservice
echo "... done."
