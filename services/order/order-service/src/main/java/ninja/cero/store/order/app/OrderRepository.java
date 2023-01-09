package ninja.cero.store.order.app;

import ninja.cero.store.order.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderRepository extends CrudRepository<Order, String> {
}
