package ninja.cero.store.order.domain;

public record OrderRequest(String name,
                           String address,
                           String telephone,
                           String mailAddress,
                           String cardNumber,
                           String cardExpire,
                           String cardName,
                           String cartId) {
}
