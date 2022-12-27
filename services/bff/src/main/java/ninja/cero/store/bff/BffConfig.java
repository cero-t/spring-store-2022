package ninja.cero.store.bff;

import ninja.cero.store.cart.client.CartClient;
import ninja.cero.store.item.client.ItemClient;
import ninja.cero.store.order.client.OrderClient;
import ninja.cero.store.stock.client.StockClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BffConfig {
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    ItemClient itemClient(RestTemplate restTemplate, @Value("${store.urls.item}") String baseUrl) {
        return new ItemClient(restTemplate, baseUrl);
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
    OrderClient orderClient(RestTemplate restTemplate, @Value("${store.urls.order}") String baseUrl) {
        return new OrderClient(restTemplate, baseUrl);
    }
}
