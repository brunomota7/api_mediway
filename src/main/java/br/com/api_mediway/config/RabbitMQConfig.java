package br.com.api_mediway.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ========== RESET PASSWORD ==========
    @Bean
    public TopicExchange resetCodeExchange() {
        return new TopicExchange("reset-code-exchange");
    }

    @Bean
    public Queue resetCodeQueue() {
        return new Queue("reset-code-queue");
    }

    @Bean
    public Binding resetCodeBinding(Queue resetCodeQueue, TopicExchange resetCodeExchange) {
        return BindingBuilder.bind(resetCodeQueue).to(resetCodeExchange).with("reset.code");
    }

    // ========== COMMON CONFIG ==========
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
