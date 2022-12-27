package ninja.cero.store.item.client;

import ninja.cero.store.item.domain.Item;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ItemClient {
    RestTemplate restTemplate;

    String baseUrl;

    ParameterizedTypeReference<List<Item>> type = new ParameterizedTypeReference<>() {
    };

    public ItemClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Item> findAll() {
        return restTemplate.exchange(baseUrl, HttpMethod.GET, null, type).getBody();
    }

    public List<Item> findByIds(Collection<Long> ids) {
        String idString = ids.stream().map(Object::toString)
                .collect(Collectors.joining(","));
        return restTemplate.exchange(baseUrl + "/" + idString, HttpMethod.GET, null, type).getBody();
    }
}
