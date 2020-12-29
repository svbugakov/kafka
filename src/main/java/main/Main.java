package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/*
# KAFKA_ADVERTISED_HOST_NAME: localhost
docker-compose -f docker-compose-single-broker.yml up -d
docker exec -it kafka-docker_kafka_1
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
