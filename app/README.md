## Endpoints
Ping by Test Application
```
curl -v -X GET -H "Accept: application/json" -H "Content-Type: application/json" "http://localhost:8080/ping"
```

Create Event
```
curl -v -X POST -H "Content-Type: application/json" -H "user_id: 123" "http://localhost:8080/v1/create-event" -d'{JSON}'
{
    "id": "5e602c69fcf135193570f8a2",
    "user_id": 123,
    "name": "Eletro Dark",
    "description": "TecknoHouse",
    "email": "eletrodark@pvt.com.br",
    "image_url": "http://s3-sa-east-1.amazonaws.com/qagile-menu-upload/a8253bfd-e766-4a1a-9b3b-b592f2db1ac1_bzum.jpg",
    "version": 1,
    "place": {
        "address": "Rua Mariporã",
        "neighborhood": "Mariporã",
        "city": "São Paulo",
        "state": "SP",
        "location": {
            "lat": -23.3391186197085,
            "lng": -47.34459991970849
        }
    }
}
```

Update Event
```
curl -v -X PUT -H "Content-Type: application/json" -H "user_id: 123" "http://localhost:8080/v1/update-event/{id}" -d'{JSON}'
{
    "name": "Tribe 9 anos",
    "place": {
        "address": "Pedreira"
    }
}
```

Remove Event
```
curl -v -X DELETE -H "Content-Type: application/json" -H "user_id: 123" "http://localhost:8080/v1/delete-event/{id}"
```

Create Menu
```
curl -v -X POST -H "Content-Type: application/json" -H "user_id: 123" "http://localhost:8080/v1/create-menu" -d'{JSON}'
{
	"event_id": "5e10e312125f2447ce744251",
	"product": "Vodka Ciroc",
	"description": "Vodka Importada",
	"price": 10250.99
}
```

Update Menu
```
curl -v -X PUT -H "Content-Type: application/json" -H "user_id: 123" "http://localhost:8080/v1/update-menu/{id}" -d'{JSON}'
{
    "description": "Catuaba Nacional"
}
```

Remove Menu
```
curl -v -X DELETE -H "Content-Type: application/json" -H "user_id: 123" "http://localhost:8080/v1/delete-menu/{id}"
```
