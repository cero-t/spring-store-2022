package ninja.cero.store.cart.domain;

import java.util.Map;

public record CartOverview(Long cartId,
                           Map<Long, Integer> items) {
}
