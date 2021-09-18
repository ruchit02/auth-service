docker run -p 5672:5672 \
-p 15672:15672 \
--network proj-net \
--name mssg-broker \
--hostname ruchit-darji \
rabbitmq:3.9.2-management

docker run -p 8080:8080 \
--network proj-net \
--name auth-service \
-e MYSQL_USER=root \
-e MYSQL_PASSWORD=root \
-e MYSQL_URL=jdbc:mysql://photo-db-server/websecurity \
-e BROKER_HOST=192.168.99.100 -e BROKER_PORT=5672 \
-e TRANSFER_PROTOCOL=http \
-e GATEWAY_SERVICE_HOST=192.168.99.100 -e GATEWAY_SERVICE_PORT=8081 \
auth-image
































