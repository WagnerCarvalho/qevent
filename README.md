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

## Endpoints
Ping by Test Application
```
curl -v -X GET -H "Accept: application/json" -H "Content-Type: application/json" "http://localhost:8080/ping"
```

Create Event
```
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

Remove Event
```
curl -v -X DELETE -H "Content-Type: application/json" -H "USER_ID: 123" "http://localhost:8080/v1/delete-event" -d'{JSON}'
{
	"id": "5e07c4ae55ec6f749ed9bcaa"
}
```

Update Event
```
curl -v -X DELETE -H "Content-Type: application/json" -H "USER_ID: 123" "http://localhost:8080/v1/update-event" -d'{JSON}'
{
    "id": "5e10e3a17abf81169172fff5",
    "name": "Tribe 9 anos",
    "place": {
        "address": "Pedreira"
    }
}
```

Create Menu
```
curl -v -X POST -H "Content-Type: application/json" -H "USER_ID: 123" "http://localhost:8080/v1/create-menu" -d'{JSON}'
{
	"event_id": "5e10e312125f2447ce744251",
	"product": "Vodka Ciroc",
	"description": "Vodka Importada",
	"price": 10250.99
}
```

Remove Menu
```
curl -v -X DELETE -H "Content-Type: application/json" -H "USER_ID: 123" "http://localhost:8080/v1/delete-menu" -d'{JSON}'
{
    "id": "5e1243c377ca0d1b398c4c34"
}
```