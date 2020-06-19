#!/bin/sh

echo "********************************************************"
echo "Wait for mongodb to be available"
echo "********************************************************"

echo "$MONGODB_STATUS_HOST"
echo "$MONGODB_STATUS_PORT"
echo "$MONGODB_URI"

# while ! nc -z $MONGODB_STATUS_HOST $MONGODB_STATUS_PORT; do
#   printf 'mongodb is still not available. Retrying...\n'
#   sleep 3
# done

echo "********************************************************"
echo "Starting app"
echo "********************************************************"

java -Dspring.profiles.active=local \
	 -Dserver.port=$SERVER_PORT \
     -jar /usr/local/event-app/qevent-0.0.1-SNAPSHOT.jar