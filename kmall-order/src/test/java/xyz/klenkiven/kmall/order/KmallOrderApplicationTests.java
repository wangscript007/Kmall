package xyz.klenkiven.kmall.order;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class KmallOrderApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(KmallOrderApplicationTests.class);

    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;

    void contextLoads() {

    }

    void createExchange() {
        Exchange exchange = new DirectExchange(
                "hello-exchange-direct",
                true,
                false,
                null
        );
        amqpAdmin.declareExchange(exchange);
    }

    void createQueue() {
        Queue queue = new Queue(
                "hello-queue",
                true,
                false,
                false,
                null
        );
        amqpAdmin.declareQueue(queue);
    }

    void createBinding() {
        // Binding(String destination, DestinationType destinationType, String exchange, String routingKey,
        //			@Nullable Map<String, Object> arguments)
        Binding binding = new Binding(
                "hello-queue",
                Binding.DestinationType.QUEUE,
                "hello-exchange-direct",
                "hello-queue",
                null
        );
        amqpAdmin.declareBinding(binding);
    }

    void sendMessage() {
        rabbitTemplate.convertAndSend(
                "hello-exchange-direct",
                "hello-queue",
                new Date()
        );
    }

}
