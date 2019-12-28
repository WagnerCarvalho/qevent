# Getting Started

Spring Boot + Kotlin + MongoDB rest API

## Requires:
```
1. `docker-compose`
2. `java 8 JDK` 
```

## Running mongo
```
# Run in project root
$ docker-compose up -d
```

## Running app
```
# Run in project root
$ ./gradlew bootRun -Dspring.profiles.active=local
```

## Test PING Application
```
curl -v -X GET -H "Accept: application/json" -H "Content-Type: application/json" "http://localhost:8080/ping"
```

## Test KTLINT Code Style
```
$ ./gradlew ktFormat
```
