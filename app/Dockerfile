FROM openjdk:8 AS TEMP_BUILD_IMAGE
USER root

ENV APP_HOME=/usr/local/event-app
WORKDIR $APP_HOME

COPY build.gradle.kts settings.gradle.kts gradlew run.sh $APP_HOME/
COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build


FROM openjdk:8
USER root

ENV MONGODB_URI=mongodb://event-mongodb:27017/qagile
ENV APP_HOME=/usr/local/event-app
ENV ARTIFACT_DIR=/build/libs
WORKDIR $APP_HOME

COPY --from=TEMP_BUILD_IMAGE $APP_HOME/$ARTIFACT_DIR .
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/src/main/resources/application-local.properties .
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/src/main/resources/db .
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/run.sh .

RUN chmod +x run.sh
CMD ./run.sh

EXPOSE 8080

#HEALTHCHECK CMD curl --fail http://localhost:8080/quser/ping || exit 1