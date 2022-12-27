package ninja.cero.store.bff.catalog;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.sql.Date;

public record CatalogItem(@Id Long id,
                          String name,
                          String media,
                          String author,
                          BigDecimal unitPrice,
                          Date release,
                          String image,
                          boolean inStock) {
}
