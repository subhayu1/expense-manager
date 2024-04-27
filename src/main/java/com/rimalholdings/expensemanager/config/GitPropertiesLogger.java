package com.rimalholdings.expensemanager.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "ApplicationStartupLogger")
public class GitPropertiesLogger {
@PostConstruct
public void readHealthCheckText() {
	try {
	String healthCheck = new String(Files.readAllBytes(Paths.get("git-info.txt")));
	log.info(healthCheck);
	} catch (IOException e) {
	log.error("Error reading health check file", e);
	}
}
}
