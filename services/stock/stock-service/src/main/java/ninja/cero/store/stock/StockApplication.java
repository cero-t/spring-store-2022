package ninja.cero.store.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
public class StockApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockApplication.class, args);
	}
}
