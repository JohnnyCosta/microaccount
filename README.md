# Account micro service

## Requirement
- Java 11
- Gradle 4+
- Docker and Docker Compose

## Compile
```
./gradlew build
```

## Lunch
- Run docker 
```
docker-compose up -d
```
- Start python server
```
python exchange.py
```
- Run Account Application
```
cp application\account-app\build\libs\account-app-1.0.0-fat.jar .
java -jar account-app-1.0.0-fat.jar account-app-1.json
```
- Run Order Application
```
cp application\order-app\build\libs\order-app-1.0.0-fat.jar .
java -jar order-app-1.0.0-fat.jar order-app-1.json
```

## URLS
- Consul:
```
http://localhost:8500/ui
```
- Account Application:
```
localhost:8080/account
```
- Order Application 
```
localhost:8081/order
```

## Notes
- Applications can be configured by customizing the JSON files : account-app-1.json and order-app-1.json
- Postman project is available: micro.postman_collection.json
 
