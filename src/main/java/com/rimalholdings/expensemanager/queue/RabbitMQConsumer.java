package com.rimalholdings.expensemanager.queue;

import java.util.List;

import com.rimalholdings.expensemanager.data.dto.Vendor;
import com.rimalholdings.expensemanager.model.mapper.VendorServiceMapper;

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
		syncManager.sync(messages);

		return null;
		//vendorServiceMapper.saveVendors(messages)
	}
}
