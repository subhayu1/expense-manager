package com.rimalholdings.expensemanager.sync;

import java.util.List;

import com.rimalholdings.expensemanager.data.dto.Vendor;
import com.rimalholdings.expensemanager.model.mapper.VendorServiceMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "SyncManager")
public class SyncManager<T> {
private final VendorServiceMapper vendorServiceMapper;

public SyncManager(VendorServiceMapper vendorServiceMapper) {
	this.vendorServiceMapper = vendorServiceMapper;
}

public void sync(MessageWrapper<T> messageWrapper) {
	String entityName = messageWrapper.getEntityName();
	List<T> message = messageWrapper.getMessage();

	switch (entityName) {
	case "vendors":
		vendorServiceMapper.saveVendors((List<Vendor>) message);
		break;
	case "purchaseInvoices":
		log.info("Purchase Invoices");
		break;
	default:
		log.info("No entity found");
	}
}
}
