package ninja.cero.store.item.app;

import ninja.cero.store.item.domain.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ItemRepository extends ListCrudRepository<Item, Long> {
}
