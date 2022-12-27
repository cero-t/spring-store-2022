package ninja.cero.store.stock.domain;

import org.springframework.data.annotation.Id;

public record Stock(@Id Long itemId,
                    Integer quantity) {
}
