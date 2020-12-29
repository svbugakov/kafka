# kafka

https://hub.docker.com/r/wurstmeister/kafka/

KAFKA_ADVERTISED_HOST_NAME: localhost
docker-compose -f docker-compose-single-broker.yml up -d
(may define topics in docker-compose-single-broker.yml)

docker exec -it kafka-docker_kafka_1

cd opt/kafka/bin

and now in another terminal window let's produce messages to the client topic:
./kafka-console-producer.sh --broker-list localhost:9092 --topic client

message in that topic and print it out when a new message is sent:
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic client --from-beginning
