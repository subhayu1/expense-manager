package com.rimalholdings.expensemanager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppConfig {
  @Value("${sync-service.vendor-url}")
  private String vendorUrl;

}
