package ninja.cero.store.cart.domain;

import java.math.BigDecimal;
import java.util.List;

public record CartDetail(Long cartId,
                         List<CartItem> items,
                         BigDecimal total) {
}
