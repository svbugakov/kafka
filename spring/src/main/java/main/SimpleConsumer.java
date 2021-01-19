package main;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SimpleConsumer {
    @KafkaListener(id = "ListenerSimpleConsumer", containerFactory = "kafkaListenerContainerFactory", topics = {"client", "zoo"})
    public void consumeMessage(String message) {
        System.out.println("try handled" + message);
        if (message.startsWith("fail")) {
            throw new RuntimeException("Soul Error!");
        }
        System.out.println("Got message soul: " + message);
    }
}
