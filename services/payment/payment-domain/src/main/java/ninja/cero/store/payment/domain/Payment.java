package ninja.cero.store.payment.domain;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public record Payment(@Id Long id,
                      String cardNumber,
                      String expire,
                      String name,
                      BigDecimal amount) {
}
