package kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class ConsumerTS implements Runnable {

    private final static String TOPIC = "client";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final Consumer<String, String> consumer;
    private String name;

    public ConsumerTS(final Consumer<String, String> consumer, final String name) {
        this.consumer = consumer;
        this.name = name;
    }

    @Override
    public void run() {
        final int giveUp = 10;   int noRecordsCount = 0;

        while (true) {
            System.out.printf("poll %s...", name);
            System.out.println();
            final ConsumerRecords<String, String> consumerRecords =
                    consumer.poll(1000);

            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp && name.equals("one"))
                    break;
                else
                    continue;
            }

            consumerRecords.forEach(record -> {
                System.out.printf("Consumer %s Record:(%s, %s, %d, %d)\n",
                        name,
                        record.key(), record.value(),
                        record.partition(), record.offset());
            });

            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE");
    }
}
