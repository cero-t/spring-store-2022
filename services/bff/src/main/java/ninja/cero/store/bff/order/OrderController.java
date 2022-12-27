package ninja.cero.store.bff.order;

import ninja.cero.store.order.client.OrderClient;
import ninja.cero.store.order.domain.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	OrderClient orderClient;

	public OrderController(OrderClient orderClient) {
		this.orderClient = orderClient;
	}

	@PostMapping
	public void checkout(@RequestBody OrderRequest order) {
		logger.info("CHECKOUT");

		orderClient.createOrder(order);
	}
}
