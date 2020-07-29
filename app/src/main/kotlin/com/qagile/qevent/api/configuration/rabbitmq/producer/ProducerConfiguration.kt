package com.qagile.qevent.api.configuration.rabbitmq.producer

import com.qagile.qevent.api.configuration.rabbitmq.entities.RabbitConstants
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableRabbit
@Configuration
class ProducerConfiguration {

    @Bean
    fun createDirectQueue(): Queue? {
        return Queue(RabbitConstants.QEVENT_ACQUIRER_QUEUE, true)
    }

    @Bean
    fun createExchange(): DirectExchange? {
        return DirectExchange(RabbitConstants.QEVENT_ACQUIRER_EX)
    }

    @Bean
    fun createBingDirect(): Binding? {
        return BindingBuilder.bind(createDirectQueue()).to(createExchange()).with(RabbitConstants.QEVENT_ACQUIRER_ROUTING_KEY)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter()
        return rabbitTemplate
    }

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }
}
