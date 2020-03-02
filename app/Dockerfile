FROM gradle:4.3.0-jdk8-alpine

EXPOSE 80
EXPOSE 443

#RUN mkdir /code
#COPY build/libs /code
#
#ENTRYPOINT [ "sh", "-c", "java -jar -Duser.timezone=$TIMEZONE -Dnetworkaddress.cache.ttl=60 -Dnetworkaddress.cache.negative.ttl=30 -Xms1024m -Xmx2048m -server -XX:+UseG1GC -XX:MaxGCPauseMillis=200 /code/*.jar" ]