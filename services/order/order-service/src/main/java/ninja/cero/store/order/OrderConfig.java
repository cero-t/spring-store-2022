package ninja.cero.store.order;

import ninja.cero.store.cart.client.CartClient;
import ninja.cero.store.order.client.OrderProcessClient;
import ninja.cero.store.payment.client.PaymentClient;
import ninja.cero.store.stock.client.StockClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderConfig {
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    StockClient stockClient(RestTemplate restTemplate, @Value("${store.urls.stock}") String baseUrl) {
        return new StockClient(restTemplate, baseUrl);
    }

    @Bean
    CartClient cartClient(RestTemplate restTemplate, @Value("${store.urls.cart}") String baseUrl) {
        return new CartClient(restTemplate, baseUrl);
    }

    @Bean
    PaymentClient paymentClient(RestTemplate restTemplate, @Value("${store.urls.payment}") String baseUrl) {
        return new PaymentClient(restTemplate, baseUrl);
    }

    @Bean
    OrderProcessClient orderProcessClient(RestTemplate restTemplate, @Value("${store.urls.order-process}") String baseUrl) {
        return new OrderProcessClient(restTemplate, baseUrl);
    }
}
