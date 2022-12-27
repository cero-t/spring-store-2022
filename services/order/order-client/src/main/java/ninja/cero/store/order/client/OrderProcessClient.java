package ninja.cero.store.order.client;

import ninja.cero.store.order.domain.OrderProcess;
import org.springframework.web.client.RestTemplate;

public class OrderProcessClient {
    RestTemplate restTemplate;

    String baseUrl;

    public OrderProcessClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void processOrder(OrderProcess order) {
        restTemplate.postForObject(baseUrl, order, Void.class);
    }
}
