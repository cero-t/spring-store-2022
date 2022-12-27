package ninja.cero.store.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("store.messaging")
public record StoreMessagingConfig(Map<String, List<String>> destinations) {
}
