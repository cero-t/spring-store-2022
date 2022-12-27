package ninja.cero.store.order.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record OrderEvent(@Id Long id,
                         Long orderId,
                         EventType eventType,
                         LocalDateTime eventTime) {
}
