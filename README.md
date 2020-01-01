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
$ ./gradlew test
```

## KtLint Validation
```
# Run in project root
$ ./gradlew ktFormat
$ ./gradlew ktLint
```

## Endpoints
```
Test Ping Application
curl -v -X GET -H "Accept: application/json" -H "Content-Type: application/json" "http://localhost:8080/ping"
```

```
Test Create Event V1
curl -v -X POST -H "Content-Type: application/json" -H "USER_ID: 123" "http://localhost:8080/v1/create-event" -d'{JSON}'

{
    "name": "Festa de 18 anos",
    "description": "Festa de niver 18anos",
    "email": "test@test.com.br",
    "image_url": "http://test.test.com.br",
    "place": {
        "address": "Rua Pinheiros",
        "neighborhood": "Luzia 13",
        "city": "Diamantina",
        "state": "MG",
        "location": {
            "lat": -18.083333,
            "lng": -43.633333
        }
    }
}
```

```
Test Remove Event V1
curl -v -X DELETE -H "Content-Type: application/json" -H "USER_ID: 123" "http://localhost:8080/v1/delete-event" -d'{JSON}'

{
	"id": "5e07c4ae55ec6f749ed9bcaa"
}
```

