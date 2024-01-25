package com.rimalholdings.expensemanager.http;

import java.util.Collections;
import java.util.List;

import com.rimalholdings.expensemanager.config.AppConfig;
import com.rimalholdings.expensemanager.data.dto.Vendor;
import com.rimalholdings.expensemanager.data.dto.sync.VendorResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j(topic = "RequestHandler")
public class RequestHandler {
private final RestTemplate restTemplate;
private final AppConfig appConfig;

public RequestHandler(RestTemplate restTemplate, AppConfig appConfig) {
	this.restTemplate = restTemplate;
	//    this.restTemplate = restTemplate;
	this.appConfig = appConfig;
}

public List<Vendor> getVendorsFromSyncService(
	Integer externalOrgId, String lastModifiedDateTime) {
	// TODO:figure out how to get the restTemplate bean for local profile when load balancer is not
	// needed
	String url =
		String.format(
			"%s?organizationId=%d&lastModifiedDateTime=%s",
			appConfig.getVendorUrl(), externalOrgId, lastModifiedDateTime);

	ResponseEntity<VendorResponse> response = restTemplate.getForEntity(url, VendorResponse.class);

	return response.getBody() != null && !response.getBody().getVendors().isEmpty()
		? response.getBody().getVendors()
		: Collections.emptyList();
}
}
