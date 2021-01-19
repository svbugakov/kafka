package kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final static String TOPIC = "client";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private static Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());

        // Create the consumer using props.
        final Consumer<String, String> consumer =
                new KafkaConsumer<>(props);

        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }

    public void runConsumer() {

        final Consumer<String, String> consumer = createConsumer();

        final int giveUp = 1000;
        int noRecordsCount = 0;

        while (true) {
            System.out.println("poll...");
            final ConsumerRecords<String, String> consumerRecords =
                    consumer.poll(1000);

            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                        record.key(), record.value(),
                        record.partition(), record.offset());
            });

            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE");
    }

    public static void main(String[] args) {
        final Consumer<String, String> consumer1 = createConsumer();
        final Consumer<String, String> consumer2 = createConsumer();
        final Consumer<String, String> consumer3 = createConsumer();

        ConsumerTS consumerTS1 = new ConsumerTS(consumer1, "one");

        ConsumerTS consumerTS2 = new ConsumerTS(consumer2, "two");
        ConsumerTS consumerTS3 = new ConsumerTS(consumer3, "three");

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(consumerTS1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.execute(consumerTS2);
        executorService.execute(consumerTS3);

        executorService.shutdown();
    }
}
