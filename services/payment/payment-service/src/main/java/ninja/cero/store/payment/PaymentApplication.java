package ninja.cero.store.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
public class PaymentApplication {
	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}
}
