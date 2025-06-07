package aba3.lucid.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter jsonConvertor() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         @Qualifier("jsonConvertor")      Jackson2JsonMessageConverter conv
                                         ) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            @Qualifier("jsonConvertor") Jackson2JsonMessageConverter conv
    ) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        f.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return f;
    }

    @Bean
    public Queue snapshotQueue() {
        return new Queue("snapshot.request.queue");
    }

    @Bean
    public Queue snapshotResponseQueue() {
        return new Queue("snapshot.response.queue");
    }

    @Bean
    public Binding snapshotRequestBinding(Queue snapshotQueue, DirectExchange toCompanyExchange) {
        return BindingBuilder.bind(snapshotQueue).to(toCompanyExchange).with("product.verify");
    }

    @Bean
    public Binding snapshotResponseBinding(Queue snapshotQueue, DirectExchange toUserExchange) {
        return BindingBuilder.bind(snapshotQueue).to(toUserExchange).with("product.verify.response");
    }

    @Bean
    public Queue scheduleQueue() {
        return new Queue("schedule.request.queue");
    }

    @Bean
    public Binding scheduleQueueBinding(Queue scheduleQueue, DirectExchange toCompanyExchange) {
        return BindingBuilder.bind(scheduleQueue).to(toCompanyExchange).with("company.schedule");
    }

    @Bean
    public Queue couponRequestQueue() {
        return new Queue("to.coupon");
    }

    @Bean
    public Queue couponResponseQueue() {
        return new Queue("from.coupon");
    }

    @Bean
    public Binding couponRequestBinding(Queue couponRequestQueue, DirectExchange toCompanyExchange) {
        return BindingBuilder.bind(couponRequestQueue).to(toCompanyExchange).with("coupon.verify");
    }

    @Bean
    public Binding couponResponseBinding(Queue couponResponseQueue, DirectExchange toUserExchange) {
        return BindingBuilder.bind(couponResponseQueue).to(toUserExchange).with("coupon.verify.response");
    }

    @Bean
    public Queue reviewCommentQueue() {
        return new Queue("review.comment.queue");
    }

    @Bean
    public TopicExchange reviewCommentExchange() {
        return new TopicExchange("review.comment.exchange");
    }


    @Bean
    public Binding reviewCommentBinding(Queue reviewCommentQueue, TopicExchange reviewCommentExchange) {
        return BindingBuilder.bind(reviewCommentQueue).to(reviewCommentExchange).with("review.comment.#");
    }

    @Bean
    public Queue commentDeleteQueue() {
        return new Queue("comment.delete.queue");
    }

    @Bean
    public TopicExchange commentDeleteExchange() {
        return new TopicExchange("comment.delete.exchange");
    }

    @Bean
    public Binding commentDeleteBinding(Queue commentDeleteQueue, TopicExchange commentDeleteExchange) {
        return BindingBuilder.bind(commentDeleteQueue).to(commentDeleteExchange).with("comment.delete.#");
    }

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
