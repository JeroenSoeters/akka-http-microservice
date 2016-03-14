FROM java:openjdk-8-jre
# Install unzip
RUN apt-get install unzip

# Add and unzip the app
ADD manufacturing-microservice-*.zip /opt/
RUN unzip /opt/manufacturing-microservice-*.zip -d /opt

# Give exec permission to the start script (unzip doesn't preserve permissions)
RUN chmod 755 /opt/manufacturing-microservice-*/bin/manufacturing-microservice

# Play port to expose
EXPOSE 9000

CMD nohup /opt/manufacturing-microservice-*/bin/manufacturing-microservice > /opt/manufacturing-microservice.log
