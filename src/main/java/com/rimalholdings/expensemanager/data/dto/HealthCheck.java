package com.rimalholdings.expensemanager.data.dto;

import lombok.Data;

@Data
public class HealthCheck {
private String commit_hash;
private String branch_name;
private String commit_message;
private String commit_date;
}
