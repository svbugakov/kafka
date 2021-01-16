# kafka

https://github.com/wurstmeister/kafka-docker.git


KAFKA_ADVERTISED_HOST_NAME: localhost
docker-compose -f docker-compose-single-broker.yml up -d
(may define topics in docker-compose-single-broker.yml)

docker exec -it kafka-docker_zookeeper_1 /bin/sh

cd opt/kafka/bin

and now in another terminal window let's produce messages to the client topic:
./kafka-console-producer.sh --broker-list localhost:9092 --topic client

message in that topic and print it out when a new message is sent:
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic client --from-beginning
