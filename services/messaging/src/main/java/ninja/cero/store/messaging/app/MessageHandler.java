package ninja.cero.store.messaging.app;

import ninja.cero.store.messaging.StoreMessaging;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class MessageHandler {
    WebClient webClient;

    StoreMessaging storeMessaging;

    public MessageHandler(WebClient webClient, StoreMessaging storeMessaging) {
        this.webClient = webClient;
        this.storeMessaging = storeMessaging;
    }

    @RabbitListener(queues = "messaging")
    void listen(Message<String> message) {
        String type = message.getHeaders().get("type", String.class);
        List<String> destinations = storeMessaging.destinations().get(type);

        destinations.forEach(destination -> webClient.post()
                .uri(destination)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(message.getPayload())
                .retrieve()
                .toEntity(String.class)
                .subscribe());
    }
}
