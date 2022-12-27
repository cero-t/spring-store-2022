package ninja.cero.store.messaging.app;

import ninja.cero.store.messaging.StoreMessagingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
