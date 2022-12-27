package ninja.cero.store.messaging.app;

import ninja.cero.store.messaging.StoreMessagingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.function.Consumer;

@Configuration
public class MessageHandler {
    WebClient webClient;

    StoreMessagingConfig storeMessagingConfig;

    public MessageHandler(WebClient webClient, StoreMessagingConfig storeMessagingConfig) {
        this.webClient = webClient;
        this.storeMessagingConfig = storeMessagingConfig;
    }

    @Bean
    Consumer<Message<String>> orderProcessConsumer() {
        return consumer("order-process");
    }

    Consumer<Message<String>> consumer(String exchange) {
        return message -> {
            List<String> destinations = storeMessagingConfig.destinations().get(exchange);

            destinations.forEach(destination -> {
                webClient.post()
                        .uri(destination)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(message.getPayload())
                        .retrieve()
                        .toEntity(String.class)
                        .subscribe();
            });
        };
    }
}
