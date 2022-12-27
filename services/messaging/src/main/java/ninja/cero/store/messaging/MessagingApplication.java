package ninja.cero.store.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MessagingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessagingApplication.class, args);
	}

	@Bean
	WebClient webClient(Builder builder) {
		return builder.build();
	}
}
