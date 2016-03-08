#!/bin/bash

echo 'Refreshing services'
vagrant ssh << EOF
cd /vagrant
sudo mkdir -p /thoughtworks/config
sudo cp conf/application.conf /thoughtworks/config/application.conf
sudo docker-compose stop scala-microservice
yes | sudo docker-compose rm
sudo docker-compose build scala-microservice
sudo docker-compose up -d scala-microservice
EOF
exit
echo 'Done'

