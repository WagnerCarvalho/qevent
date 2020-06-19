# qevent
MS to Created Event and Menu

---
|  CircleCI         | Code Style   | Docker Hub (Master)
|----------|:---------------------------:|:----------------------------:|
| [![Build status](https://circleci.com/gh/WagnerCarvalho/qevent.svg?style=svg&circle-token=b25a2e24c7c0cf622487450fd4248b1574417c81)](https://circleci.com/gh/WagnerCarvalho/qevent) | [![Code Style](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/) | [![Docker Hub](https://dockeri.co/image/qagile/qevent)](https://hub.docker.com/repository/docker/qagile/qevent)


# Getting Started

Spring Boot + Kotlin + MongoDB rest API

## Requires:
```
1. `docker-compose`
2. `java 8 JDK` 
```

## Running ALL (Application + Mongo + Nginx)
```
# Run in qevent
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

## Swagger
> [Api Swagger](http://18.230.85.32:8000/qevent/swagger-ui.html)
