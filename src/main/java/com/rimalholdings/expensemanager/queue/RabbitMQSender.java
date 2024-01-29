package com.rimalholdings.expensemanager.queue;

import com.rimalholdings.expensemanager.config.AppConfig;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
private final RabbitTemplate rabbitTemplate;
private final AppConfig appConfig;

public RabbitMQSender(RabbitTemplate rabbitTemplate, AppConfig appConfig) {
	this.rabbitTemplate = rabbitTemplate;
	this.appConfig = appConfig;
}

public void send(Integer message) {
	rabbitTemplate.convertAndSend(appConfig.getExchange(), appConfig.getRoutingKey(), message);
}
}
