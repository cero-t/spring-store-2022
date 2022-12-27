package ninja.cero.store.order.domain;

import ninja.cero.store.cart.domain.CartDetail;

public record OrderProcess(OrderRequest orderRequest, CartDetail cartDetail) {
}
