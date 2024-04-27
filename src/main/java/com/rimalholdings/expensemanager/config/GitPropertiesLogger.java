package com.rimalholdings.expensemanager.config;

import java.util.HashMap;
import java.util.Map;

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
}
