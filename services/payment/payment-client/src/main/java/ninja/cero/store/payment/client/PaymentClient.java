package ninja.cero.store.payment.client;

import ninja.cero.store.payment.domain.Payment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PaymentClient {
    RestTemplate restTemplate;

    String baseUrl;

    ParameterizedTypeReference<List<Payment>> type = new ParameterizedTypeReference<>() {
    };

    public PaymentClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void check(Payment payment) {
        restTemplate.postForObject(baseUrl + "/check", payment, Void.class);
    }

    public void processPayment(Payment payment) {
        restTemplate.postForObject(baseUrl, payment, Void.class);
    }

    public List<Payment> findAll() {
        return restTemplate.exchange(baseUrl, HttpMethod.GET, null, type).getBody();
    }
}
