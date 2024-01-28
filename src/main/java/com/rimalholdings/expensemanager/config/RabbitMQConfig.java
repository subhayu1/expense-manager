package com.rimalholdings.expensemanager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
private final AppConfig appConfig;

public RabbitMQConfig(AppConfig appConfig) {
	this.appConfig = appConfig;
}

@Bean
public Queue queue() {
	return new Queue(appConfig.getQueue(), false);
}

@Bean
public TopicExchange exchange() {
	return new TopicExchange(appConfig.getExchange());
}

@Bean
public Binding binding(Queue queue, TopicExchange exchange) {
	return BindingBuilder.bind(queue).to(exchange).with("sync.#");
}

@Bean
public MessageConverter jsonMessageConverter() {
	Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
	DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
	javaTypeMapper.setTrustedPackages(
		"*");
	jsonConverter.setJavaTypeMapper(javaTypeMapper);
	return jsonConverter;
}
}
