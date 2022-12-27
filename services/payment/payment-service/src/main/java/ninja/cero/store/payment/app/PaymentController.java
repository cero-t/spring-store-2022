package ninja.cero.store.payment.app;

import ninja.cero.store.order.domain.OrderProcess;
import ninja.cero.store.order.domain.OrderRequest;
import ninja.cero.store.payment.domain.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@RestController
public class PaymentController {
    PaymentRepository paymentRepository;

    DateTimeFormatter expiredFormatter = DateTimeFormatter.ofPattern("MM/yy");

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/check")
    public void check(@RequestBody Payment payment) {
        YearMonth yearMonth = YearMonth.parse(payment.expire(), expiredFormatter);

        if (yearMonth.isBefore(YearMonth.now())) {
            throw new RuntimeException("Card is expired.");
        }
    }

    @PostMapping("/")
    public void processPayment(@RequestBody Payment payment) {
        paymentRepository.save(payment);
    }

    @GetMapping("/")
    public Iterable<Payment> payments() {
        return paymentRepository.findAll();
    }

    @PostMapping("/order")
    public void processOrder(@RequestBody OrderProcess order) {
        OrderRequest request = order.orderRequest();

        Payment payment = new Payment(null, request.cardName(), request.cardExpire(), request.cardNumber(),
                order.cartDetail().total());
        paymentRepository.save(payment);
    }
}
