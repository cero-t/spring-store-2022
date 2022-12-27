package ninja.cero.store.cart.client;

import ninja.cero.store.cart.domain.CartDetail;
import ninja.cero.store.cart.domain.CartEvent;
import ninja.cero.store.cart.domain.CartOverview;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CartClient {
    RestTemplate restTemplate;

    String baseUrl;

    ParameterizedTypeReference<List<CartOverview>> type = new ParameterizedTypeReference<>() {
    };

    public CartClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<CartOverview> findAll() {
        return restTemplate.exchange(baseUrl, HttpMethod.GET, null, type).getBody();
    }

    public CartOverview findCartById(String cartId) {
        return restTemplate.getForObject(baseUrl + "/" + cartId, CartOverview.class);
    }

    public CartDetail findCartDetailById(String cartId) {
        return restTemplate.getForObject(baseUrl + "/" + cartId + "/detail", CartDetail.class);
    }

    public CartOverview createCart() {
        return restTemplate.postForObject(baseUrl, null, CartOverview.class);
    }

    public CartOverview addItem(String cartId, CartEvent cartEvent) {
        return restTemplate.postForObject(baseUrl + "/" + cartId, cartEvent, CartOverview.class);
    }

    public void removeItem(String cartId, Long itemId) {
        restTemplate.delete(baseUrl + "/" + cartId + "/" + "items" + "/" + itemId);
    }
}
