Simple stock exchange system

how to up?
1. Use any kafka image to up kafka in docker(user default config of broker)
2. Go inside each service and up one by one using command (mvn clean install -> mvn spring-boot:run)
3. once all 3 services are up use below curl to place individual buy/sell orders.
4. Currently queue size for matching orders is 3, it can be increased to the need.
   curl --location 'http://localhost:8080/api/kafka/orders' \
--header 'Content-Type: application/json' \
--data '{
  "type": "sell",
  "price": 101.95,
  "quantity": 150

}'

curl --location 'http://localhost:8080/api/kafka/orders' \
--header 'Content-Type: application/json' \
--data '{
  "type": "buy",
  "price": 100.95,
  "quantity": 150
}'

what is left?
1. Storing each matched records into database
2. Storing price/time data in timeseries db as well.
3. Send notification to users
4. Send current price updates through websocket to users
