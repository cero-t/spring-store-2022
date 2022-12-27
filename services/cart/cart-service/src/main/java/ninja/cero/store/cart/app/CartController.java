package ninja.cero.store.cart.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ninja.cero.store.cart.domain.CartDetail;
import ninja.cero.store.cart.domain.CartEvent;
import ninja.cero.store.cart.domain.CartItem;
import ninja.cero.store.cart.domain.CartOverview;
import ninja.cero.store.item.client.ItemClient;
import ninja.cero.store.item.domain.Item;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CartController {
    CartRepository cartRepository;

    ItemClient itemClient;

    ObjectMapper mapper = new ObjectMapper();

    public CartController(CartRepository cartRepository, ItemClient itemClient) {
        this.cartRepository = cartRepository;
        this.itemClient = itemClient;
    }

    @GetMapping("/")
    public List<CartOverview> findAll() {
        return StreamSupport.stream(cartRepository.findAll().spliterator(), false)
                .map(this::toCartOverview)
                .collect(Collectors.toList());
    }

    @GetMapping("/{cartId}")
    public Optional<CartOverview> findCartById(@PathVariable Long cartId) {
        return cartRepository.findById(cartId)
                .map(this::toCartOverview);
    }

    @GetMapping("/{cartId}/detail")
    public CartDetail findCartDetailById(@PathVariable Long cartId) {
        // Create cart
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        CartOverview cartOverview = toCartOverview(cart);

        // Find items in cart and convert to map
        Map<Long, Item> itemMap = itemClient.findByIds(cartOverview.items().keySet())
                .stream()
                .collect(Collectors.toMap(Item::id, i -> i));

        // Resolve cart items
        List<CartItem> items = cartOverview.items()
                .entrySet()
                .stream()
                .map(i -> {
                    Item item = itemMap.get(i.getKey());
                    return new CartItem(item.id(),
                            item.name(),
                            item.author(),
                            item.unitPrice(),
                            item.release(),
                            item.image(),
                            i.getValue());
                }).toList();

        BigDecimal total = items.stream()
                .map(i -> i.unitPrice().multiply(new BigDecimal(i.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return new CartDetail(cart.id(), items, total);
    }

    private CartOverview toCartOverview(Cart cart) {
        if (cart.items() == null) {
            return new CartOverview(cart.id(), new HashMap<>());
        }

        try {
            Map<?, ?> map = mapper.readValue(cart.items(), Map.class);
            Map<Long, Integer> items = map.entrySet().stream()
                    .collect(Collectors.toMap(e -> Long.valueOf(e.getKey().toString()),
                            e -> Integer.valueOf(e.getValue().toString())));
            return new CartOverview(cart.id(), items);
        } catch (IOException ex) {
            throw new RuntimeException("Json deserialize error", ex);
        }
    }

    private Cart toEntity(CartOverview cartOverview) {
        try {
            String items = mapper.writeValueAsString(cartOverview.items());
            return new Cart(cartOverview.cartId(), items);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json serialize error", ex);
        }
    }

    @PostMapping("/")
    public CartOverview createCart() {
        Cart created = cartRepository.save(new Cart(null, null));
        return new CartOverview(created.id(), new LinkedHashMap<>());
    }

    @PostMapping("/{cartId}")
    public CartOverview addItem(@PathVariable Long cartId, @RequestBody CartEvent cartEvent) {
        CartOverview cartOverview = cartRepository.findById(cartId)
                .map(this::toCartOverview)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartOverview.items()
                .compute(cartEvent.itemId(), (key, old) -> old == null ? cartEvent.quantity() : old + cartEvent.quantity());
        cartRepository.save(toEntity(cartOverview));

        return cartOverview;
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public CartOverview removeItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        CartOverview cartOverview = cartRepository.findById(cartId)
                .map(this::toCartOverview)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartOverview.items().remove(itemId);
        cartRepository.save(toEntity(cartOverview));

        return cartOverview;
    }
}
