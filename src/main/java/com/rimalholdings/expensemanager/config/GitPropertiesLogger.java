package com.rimalholdings.expensemanager.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.rimalholdings.expensemanager.data.dto.HealthCheck;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "ApplicationStartupLogger")
@PropertySource("classpath:git.properties")
public class GitPropertiesLogger {
@Value("${git.commit.id}")
private String commitId;

@Value("${git.branch}")
private String branch;

@Value("${git.commit.message.full}")
private String commitMessage;

@Value("${git.build.host}")
private String buildHost;

@Value("${git.commit.time}")
private String commitTime;

@PostConstruct
public void logGitProperties() {
	Map<String, String> gitProperties = new HashMap<>();
	gitProperties.put("commitId", commitId);
	gitProperties.put("branch", branch);
	gitProperties.put("commitMessage", commitMessage);
	gitProperties.put("buildHost", buildHost);
	gitProperties.put("commitTime", commitTime);
	log.info(gitProperties.toString());
}

// @PostConstruct
public void readHealthCheck() {
	// read the health check.json file using object mapper
	// log the health check details
	ObjectMapper objectMapper = new ObjectMapper();
	try {
	HealthCheck healthCheck =
		objectMapper.readValue(new File("health-check.json"), HealthCheck.class);
	log.info(healthCheck.toString());
	} catch (IOException e) {
	log.error("Error reading health check file", e);
	}
}

@PostConstruct
public void readHealthCheckText() {
	// read the health check.txt file using object mapper
	// log the health check details
	try {
	String healthCheck = new String(Files.readAllBytes(Paths.get("git-info.txt")));
	log.info(healthCheck);
	} catch (IOException e) {
	log.error("Error reading health check file", e);
	}
}
}
