package com.rimalholdings.expensemanager.specification;

import com.rimalholdings.expensemanager.data.dto.Expense;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;

import org.springframework.data.jpa.domain.Specification;

public class ExpenseSpecification {
public static Specification<Expense> hasVendorId(Long vendorId) {
	return (root, query, cb) -> {
	if (vendorId == null) {
		return null;
	}
	return cb.equal(root.get("vendor").get("id"), vendorId);
	};
}

public static Specification<ExpenseEntity> hasExternalOrgId(Integer externalOrgId) {
	return (root, query, cb) -> {
	if (externalOrgId == null) {
		return null;
	}
	return cb.equal(root.get("externalOrgId"), externalOrgId);
	};
}
}
