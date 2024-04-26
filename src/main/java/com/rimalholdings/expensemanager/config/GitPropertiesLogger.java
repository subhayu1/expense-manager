package com.rimalholdings.expensemanager.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "ApplicationStartupLogger")
public class GitPropertiesLogger {

@Value("${git.commit.id:unknown}")
private String commitId;

@Value("${git.branch:unknown}")
private String branch;

@Value("${git.commit.message:unknown}")
private String commitMessage;

@Value("${git.commit.time:unknown}")
private String commitTime;

@PostConstruct
public void logGitProperties() {
	String json =
		String.format(
			"{\"commit_hash\": \"%s\", \"branch_name\": \"%s\", \"commit_message\": \"%s\", \"commit_date\": \"%s\"}",
			commitId, branch, commitMessage, commitTime);

	log.info(json);
}
}
