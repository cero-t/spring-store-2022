package ninja.cero.store.order.app;

import ninja.cero.store.cart.client.CartClient;
import ninja.cero.store.cart.domain.CartDetail;
import ninja.cero.store.order.client.OrderProcessClient;
import ninja.cero.store.order.domain.*;
import ninja.cero.store.payment.client.PaymentClient;
import ninja.cero.store.payment.domain.Payment;
import ninja.cero.store.stock.client.StockClient;
import ninja.cero.store.stock.domain.Stock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    OrderRepository orderRepository;

    OrderEventRepository orderEventRepository;

    StockClient stockClient;

    CartClient cartClient;

    PaymentClient paymentClient;

    OrderProcessClient orderProcessClient;

    public OrderController(OrderRepository orderRepository, OrderEventRepository orderEventRepository,
                           StockClient stockClient, CartClient cartClient, PaymentClient paymentClient,
                           OrderProcessClient orderProcessClient) {
        this.orderRepository = orderRepository;
        this.orderEventRepository = orderEventRepository;
        this.stockClient = stockClient;
        this.cartClient = cartClient;
        this.paymentClient = paymentClient;
        this.orderProcessClient = orderProcessClient;
    }

    @PostMapping("/")
    public void createOrder(@RequestBody OrderRequest request) {
        CartDetail cart = cartClient.findCartDetailById(request.cartId());
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        // Check card
        Payment payment = new Payment(null, request.cardName(), request.cardExpire(), request.cardNumber(), cart.total());
        paymentClient.check(payment);

        // save order request
        Order order = orderRepository.save(new Order(null, request.name(), request.address(), request.telephone(),
                request.mailAddress(), request.cardNumber(), request.cardExpire(), request.cardName(), request.cartId()));

        // Keep stock
        List<Stock> keepRequests = cart.items().stream()
                .map(i -> new Stock(i.itemId(), i.quantity()))
                .collect(Collectors.toList());
        stockClient.keepStock(keepRequests);

        // Start orderEvent
        OrderEvent event = new OrderEvent(null, order.id(), EventType.START, LocalDateTime.now());
        orderEventRepository.save(event);

        // Process Order
        OrderProcess orderProcess = new OrderProcess(order, cart);
        orderProcessClient.processOrder(orderProcess);
    }

    @PostMapping("/{orderId}/event")
    public void createEvent(@RequestBody OrderEvent orderEvent) {
        orderEventRepository.save(orderEvent);
    }

    @GetMapping("/")
    public Iterable<Order> getOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/events")
    public Iterable<OrderEvent> getEvents() {
        return orderEventRepository.findAll();
    }
}
