package ninja.cero.store.bff.cart;

import ninja.cero.store.cart.client.CartClient;
import ninja.cero.store.cart.domain.CartDetail;
import ninja.cero.store.cart.domain.CartEvent;
import ninja.cero.store.cart.domain.CartOverview;
import ninja.cero.store.stock.client.StockClient;
import ninja.cero.store.stock.domain.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    CartClient cartClient;

    StockClient stockClient;

    public CartController(CartClient cartClient, StockClient stockClient) {
        this.cartClient = cartClient;
        this.stockClient = stockClient;
    }

    @PostMapping
    public CartOverview createCart() {
        logger.info("CART CREATED");
        return cartClient.createCart();
    }

    @GetMapping("/{cartId}")
    public CartDetail findCart(@PathVariable String cartId) {
        logger.info("CART DETAIL");
        return cartClient.findCartDetailById(cartId);
    }

    @PostMapping("/{cartId}")
    public CartDetail addEvent(@PathVariable String cartId, @RequestBody CartEvent cartEvent) {
        logger.info("ADD ITEM");

        CartOverview cart = cartClient.findCartById(cartId);
        if (cart == null) {
            throw new RuntimeException("No valid cart");
        }

        List<Stock> stocks = stockClient.findByIds(Collections.singletonList(cartEvent.itemId()));
        if (stocks.isEmpty()) {
            throw new RuntimeException("No stock info!");
        }

        if (stocks.get(0).quantity() < cartEvent.quantity()) {
            throw new RuntimeException("Not enough stock!");
        }

        cartClient.addItem(cartId, cartEvent);
        return cartClient.findCartDetailById(cartId);
    }

    @DeleteMapping("/{cartId}/{itemId}")
    public CartDetail removeItem(@PathVariable String cartId, @PathVariable Long itemId) {
        logger.info("ITEM REMOVED");

        cartClient.removeItem(cartId, itemId);
        return cartClient.findCartDetailById(cartId);
    }
}
