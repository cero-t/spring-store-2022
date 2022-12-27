package ninja.cero.store.order.domain;

import org.springframework.data.annotation.Id;

public record OrderRequest(@Id Long id,
                           String name,
                           String address,
                           String telephone,
                           String mailAddress,
                           String cardNumber,
                           String cardExpire,
                           String cardName,
                           String cartId) {
}
