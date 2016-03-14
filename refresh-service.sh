#!/bin/bash

echo 'Refreshing manufacturing-microservice'
docker-compose stop manufacturing-microservice
yes | docker-compose rm
docker-compose -p xplanning build manufacturing-microservice
docker-compose up -d manufacturing-microservice
echo 'Done'

echo 'Refreshing services'


