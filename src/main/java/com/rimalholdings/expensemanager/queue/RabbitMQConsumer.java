package com.rimalholdings.expensemanager.queue;

import java.util.List;

import com.rimalholdings.expensemanager.data.dto.Vendor;
import com.rimalholdings.expensemanager.model.mapper.VendorServiceMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "RabbitMQConsumer")
public class RabbitMQConsumer {
private final VendorServiceMapper vendorServiceMapper;

public RabbitMQConsumer(VendorServiceMapper vendorServiceMapper) {
	this.vendorServiceMapper = vendorServiceMapper;
}

@RabbitListener(queues = "syncQueue")
public void consume(List<Vendor> messages) {
	log.info("Received message: {}", messages);
	vendorServiceMapper.saveVendors(messages);
}
}
