package ninja.cero.store.order.app;

import ninja.cero.store.order.domain.OrderRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderRequestRepository extends CrudRepository<OrderRequest, String> {
}
