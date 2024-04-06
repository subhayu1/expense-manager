package com.rimalholdings.expensemanager.queue;

import com.rimalholdings.expensemanager.sync.MessageWrapper;
import com.rimalholdings.expensemanager.sync.SyncManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "RabbitMQConsumer")
public class RabbitMQConsumer<T> {
private final SyncManager<T> syncManager;

public RabbitMQConsumer(SyncManager<T> syncManager) {
	this.syncManager = syncManager;
}

@RabbitListener(queues = "syncQueue")
public MessageWrapper<T> consume(MessageWrapper<T> messages) {
	log.info("Message received from RabbitMQ");
	log.info("message: {}", messages.getMessage());
	syncManager.sync(messages);

	return null;
	// vendorServiceMapper.saveVendors(messages)
}
}
