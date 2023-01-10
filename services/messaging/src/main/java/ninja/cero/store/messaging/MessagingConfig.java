package ninja.cero.store.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MessagingConfig implements BeanPostProcessor {
    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    RabbitAdmin amqpAdmin(RabbitTemplate rabbitTemplate) {
        return new RabbitAdmin(rabbitTemplate);
    }

    @Bean
    Queue queue() {
        return new Queue("messaging");
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof RabbitTemplate template) {
            template.setObservationEnabled(true);
        } else if (bean instanceof SimpleRabbitListenerContainerFactory factory) {
            factory.setObservationEnabled(true);
        }

        return bean;
    }
}
