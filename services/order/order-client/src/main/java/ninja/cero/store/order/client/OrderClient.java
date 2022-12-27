package ninja.cero.store.order.client;

import ninja.cero.store.order.domain.OrderEvent;
import ninja.cero.store.order.domain.OrderRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class OrderClient {
    RestTemplate restTemplate;

    String baseUrl;

    ParameterizedTypeReference<List<OrderEvent>> type = new ParameterizedTypeReference<>() {
    };

    public OrderClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void createOrder(OrderRequest order) {
        restTemplate.postForObject(baseUrl, order, Void.class);
    }

    public void createEvent(OrderEvent orderEvent) {
        restTemplate.postForObject(baseUrl + "/" + orderEvent.orderId() + "/event", orderEvent, Void.class);
    }

    public List<OrderEvent> findAllEvents() {
        return restTemplate.exchange(baseUrl + "/events", HttpMethod.GET, null, type).getBody();
    }
}
