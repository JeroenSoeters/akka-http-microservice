FROM ubuntu

# Add and unzip the app
ADD scala-microservice-*.zip /opt/
RUN unzip /opt/scala-microservice-*.zip -d /opt

# Give exec permission to the start script (unzip doesn't preserve permissions)
RUN chmod 755 /opt/scala-microservice-*/bin/scala-microservice

# Play port to expose
EXPOSE 9000

CMD nohup /opt/scala-microservice-*/bin/scala-microservice
