package ninja.cero.store.cart.domain;

import java.math.BigDecimal;
import java.sql.Date;

public record CartItem(Long itemId,
                       String name,
                       String author,
                       BigDecimal unitPrice,
                       Date release,
                       String image,
                       Integer quantity) {
}
