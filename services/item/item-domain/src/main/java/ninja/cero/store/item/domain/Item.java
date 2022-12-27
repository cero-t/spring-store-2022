package ninja.cero.store.item.domain;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.sql.Date;

public record Item(@Id Long id,
                   String name,
                   String media,
                   String author,
                   BigDecimal unitPrice,
                   Date release,
                   String image) {
}
