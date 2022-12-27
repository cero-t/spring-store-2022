package ninja.cero.store.cart;

import ninja.cero.store.item.client.ItemClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CartConfig {
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    ItemClient itemClient(RestTemplate restTemplate, @Value("${store.urls.item}") String baseUrl) {
        return new ItemClient(restTemplate, baseUrl);
    }
}
