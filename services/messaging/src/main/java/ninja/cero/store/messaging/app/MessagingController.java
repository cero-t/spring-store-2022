package ninja.cero.store.messaging.app;

import ninja.cero.store.messaging.StoreMessagingConfig;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagingController {
    StreamBridge streamBridge;

    StoreMessagingConfig storeMessagingConfig;

    public MessagingController(StreamBridge streamBridge, StoreMessagingConfig storeMessagingConfig) {
        this.streamBridge = streamBridge;
        this.storeMessagingConfig = storeMessagingConfig;
    }

    @PostMapping(value = {"/{exchange}"})
    public void request(@PathVariable String exchange, @RequestBody String body) {
        streamBridge.send(exchange, body);
    }
}
