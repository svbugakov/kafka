package main;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



@RunWith(SpringRunner.class)
@EmbeddedKafka(
        // We're only needing to test Kafka serializing interactions, so keep partitioning simple
        partitions = 1,
        // use some non-default topics to test via
        topics = {
                "zoo"
        },
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@SpringBootTest
public class SimpleConsumerTest {
    @Autowired
    public static EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private KafkaTemplate<String, String> template;

    @Before
    public void setup() {
        //System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
        //System.setProperty("spring.cloud.stream.kafka.binder.zkNodes", embeddedKafka.getZookeeperConnectionString());
    }

    @Test
    public void test() throws Exception {
        ConcurrentMessageListenerContainer<?, ?> container = (ConcurrentMessageListenerContainer<?, ?>) registry
                .getListenerContainer("ListenerSimpleConsumer");
        container.stop();
        @SuppressWarnings("unchecked")
        AcknowledgingConsumerAwareMessageListener<String, String> messageListener = (AcknowledgingConsumerAwareMessageListener<String, String>) container
                .getContainerProperties().getMessageListener();
        CountDownLatch latch = new CountDownLatch(1);
        container.getContainerProperties()
                .setMessageListener(new AcknowledgingConsumerAwareMessageListener<String, String>() {
                    @Override
                    public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
                        System.out.println("onmessages-----------------------------------------------------------------------");
                        messageListener.onMessage(consumerRecord, acknowledgment, consumer);
                        latch.countDown();
                    }
                });
        container.start();
        template.send("zoo", "foo");
        Thread.sleep(1000);
        System.out.println("awaits--------------------------------------------------------------------------------------");
        Assert.assertTrue(latch.await(10, TimeUnit.SECONDS));
    }

    @Test
    public void simpleTest() {


        Assert.assertTrue(true);
    }


}
