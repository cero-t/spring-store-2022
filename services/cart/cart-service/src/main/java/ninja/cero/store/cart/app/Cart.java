package ninja.cero.store.cart.app;

import org.springframework.data.annotation.Id;

public record Cart(@Id Long id,
                   String items) {
}
