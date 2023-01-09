package ninja.cero.store.messaging.app;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagingController {
    AmqpTemplate amqpTemplate;

    Queue queue;

    public MessagingController(AmqpTemplate amqpTemplate, Queue queue) {
        this.amqpTemplate = amqpTemplate;
        this.queue = queue;
    }

    @PostMapping(value = {"/{type}"})
    public void request(@PathVariable String type, @RequestBody String body) {
        Message<String> message = MessageBuilder.withPayload(body)
                .setHeader("type", type)
                .build();
        amqpTemplate.convertAndSend(queue.getName(), message);
    }
}
