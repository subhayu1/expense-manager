package com.rimalholdings.expensemanager.sync;

import lombok.Data;

import java.util.List;

@Data
public class MessageWrapper<T> {
    private String entityName;
    private List<T> message;
    private String externalOrgId;
}