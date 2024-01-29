package com.rimalholdings.expensemanager.sync;

import java.util.List;

import lombok.Data;

@Data
public class MessageWrapper<T> {
private String entityName;
private List<T> message;
private String externalOrgId;
}
