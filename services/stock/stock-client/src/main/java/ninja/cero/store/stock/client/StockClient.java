package ninja.cero.store.stock.client;

import ninja.cero.store.stock.domain.Stock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StockClient {
    RestTemplate restTemplate;

    String baseUrl;

    ParameterizedTypeReference<List<Stock>> type = new ParameterizedTypeReference<>() {
    };

    public StockClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Stock> findAll() {
        return restTemplate.exchange(baseUrl, HttpMethod.GET, null, type).getBody();
    }

    public List<Stock> findByIds(Collection<Long> ids) {
        String idString = ids.stream().map(Object::toString).collect(Collectors.joining(","));
        return restTemplate.exchange(baseUrl + "/" + idString, HttpMethod.GET, null, type).getBody();
    }

    public void keepStock(List<Stock> keeps) {
        restTemplate.postForObject(baseUrl, keeps, Void.class);
    }
}
