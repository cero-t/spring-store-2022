package ninja.cero.store.bff.catalog;

import ninja.cero.store.item.client.ItemClient;
import ninja.cero.store.item.domain.Item;
import ninja.cero.store.stock.client.StockClient;
import ninja.cero.store.stock.domain.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
@CrossOrigin
public class CatalogController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    ItemClient itemClient;

    StockClient stockClient;

    public CatalogController(ItemClient itemClient, StockClient stockClient) {
        this.itemClient = itemClient;
        this.stockClient = stockClient;
    }

    @GetMapping
    public List<CatalogItem> getCatalog() {
        logger.info("GET CATALOG");

        List<Item> items = itemClient.findAll();
        List<Long> ids = items.stream()
                .map(Item::id)
                .collect(Collectors.toList());

        List<Stock> stocks = stockClient.findByIds(ids);
        Map<Long, Integer> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::itemId, Stock::quantity));

        // Filter items by stock
        return items.stream()
                .map(item -> new CatalogItem(
                        item.id(),
                        item.name(),
                        item.media(),
                        item.author(),
                        item.unitPrice(),
                        item.release(),
                        item.image(),
                        stockMap.getOrDefault(item.id(), 0) > 0)
                ).collect(Collectors.toList());
    }
}
