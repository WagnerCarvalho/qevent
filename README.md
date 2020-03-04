# qevent
MS to Created Event and Menu

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

## Running Test Unit
```
# Run in project root
$ ./gradlew cleanTest test
```

## KtLint Validation
```
# Run in project root
$ ./gradlew ktFormat
$ ./gradlew ktLint
```
