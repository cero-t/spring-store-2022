package ninja.cero.store.delivery.app;

import ninja.cero.store.order.domain.OrderProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/")
    public void deliveryOrder(@RequestBody OrderProcess orderProcess) throws InterruptedException {
        logger.info("Delivery - started: " + orderProcess);
        Thread.sleep(200L);
    }
}
