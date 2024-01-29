package com.rimalholdings.expensemanager.sync;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public class SyncEntities {

public static Set<String> getSyncEntities() {
	Set<String> syncEntities = new HashSet<>();
	syncEntities.add("accounts");
	syncEntities.add("purchaseInvoices");
	return syncEntities;
}
}
