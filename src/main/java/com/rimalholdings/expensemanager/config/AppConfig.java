package com.rimalholdings.expensemanager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppConfig {
@Value("${sync-service.vendor-url}")
private String vendorUrl;

@Value("${rabbit.em.queue.name}")
private String queue;

@Value("${rabbit.em.exchange.name}")
private String exchange;

@Value("${rabbit.em.routing.key}")
private String routingKey;
}
