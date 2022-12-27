package ninja.cero.store.cart.domain;

public record CartEvent(Long itemId,
                        Integer quantity) {
}
