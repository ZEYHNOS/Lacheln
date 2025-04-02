package aba3.lucid.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue toCompanyQueue()   {
        return new Queue("to.company.queue");
    }

    @Bean
    public DirectExchange toCompanyExchange()   {
        return new DirectExchange("to.company.exchange");
    }

    @Bean
    public Binding toCompanyBinding(Queue toCompanyQueue, DirectExchange toCompanyExchange)   {
        return BindingBuilder.bind(toCompanyQueue).to(toCompanyExchange).with("to.company");
    }

    @Bean
    public Queue toUserQueue()  {
        return new Queue("to.user.queue");
    }

    @Bean
    public DirectExchange toUserExchange()   {
        return new DirectExchange("to.user.exchange");
    }

    @Bean
    public Binding toUserBinding(Queue toUserQueue, DirectExchange toUserExchange)   {
        return BindingBuilder.bind(toUserQueue).to(toUserExchange).with("to.user");
    }
}
