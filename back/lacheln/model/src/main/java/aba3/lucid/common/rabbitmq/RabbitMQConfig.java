package aba3.lucid.common.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue toCompanyQueue()   {
        return new Queue("to.company");
    }

    @Bean
    public Queue companyQueue()   {
        return new Queue("company");
    }

    @Bean
    public DirectExchange toCompanyExchange()   {
        return new DirectExchange("company.exchange");
    }

    @Bean
    public Binding companyBinding(Queue companyQueue, DirectExchange toCompanyExchange)   {
        return BindingBuilder.bind(companyQueue).to(toCompanyExchange).with("company");
    }

    @Bean
    public Binding toCompanyBinding(Queue toCompanyQueue, DirectExchange toCompanyExchange)   {
        return BindingBuilder.bind(toCompanyQueue).to(toCompanyExchange).with("to.company");
    }

    @Bean
    public Queue toUserQueue()  {
        return new Queue("to.user");
    }

    @Bean
    public Queue userQueue()    {
        return new Queue("user");
    }

    @Bean
    public DirectExchange toUserExchange()   {
        return new DirectExchange("user.exchange");
    }

    @Bean
    public Binding toUserBinding(Queue toUserQueue, DirectExchange toUserExchange)   {
        return BindingBuilder.bind(toUserQueue).to(toUserExchange).with("to.user");
    }

    @Bean
    public Binding userBinding(Queue userQueue, DirectExchange toUserExchange)   {
        return BindingBuilder.bind(userQueue).to(toUserExchange).with("user");
    }

    // 업체쪽 채팅 메시지 처리 큐 설정
    @Value("${rabbitmq.exchange.chat}")
    private String chatExchangeName;

    @Value("${rabbitmq.queue.chat}")
    private String chatQueueName;

    @Value("${rabbitmq.routing.chat}")
    private String chatRoutingKey;

    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange(chatExchangeName);
    }
    @Bean
    public Queue chatQueue() {
        return new Queue(chatQueueName);
    }
    @Bean
    public Binding chatBinding(Queue chatQueue, TopicExchange chatExchange) {
        return BindingBuilder.bind(chatQueue).to(chatExchange).with(chatRoutingKey);
    }
}
