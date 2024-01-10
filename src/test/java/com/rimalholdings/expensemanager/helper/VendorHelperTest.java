package com.rimalholdings.expensemanager.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VendorHelperTest {

@Test
void testGenerateVendorIdWithTwoNames() {

	String name = "name value";
	Integer zipCode = 12345;
	String result = VendorHelper.generateVendorId(name, zipCode);
	assertEquals("NV12345", result);
}

@Test
void testGenerateVendorIdWithOneName() {

	String name = "name";
	Integer zipCode = 12345;
	String result = VendorHelper.generateVendorId(name, zipCode);
	assertEquals("N12345", result);
}

@Test
void testGenerateVendorIdWithThreeNames() {

	String name = "name value other";
	Integer zipCode = 12345;
	String result = VendorHelper.generateVendorId(name, zipCode);
	assertEquals("NVO12345", result);
}

@Test
void testSanitizePhoneNumberWithDash() {
	String phoneNumber = "123-456-7890";
	String result = VendorHelper.sanitizePhoneNumber(phoneNumber);
	assertEquals("1234567890", result);
}

@Test
void testSanitizePhoneNumberWithParenthesis() {
	String phoneNumber = "(123)456-7890";
	String result = VendorHelper.sanitizePhoneNumber(phoneNumber);
	assertEquals("1234567890", result);
}

@Test
void testSanitizePhoneNumberWithDashAndParenthesis() {
	String phoneNumber = "(123)-456-7890";
	String result = VendorHelper.sanitizePhoneNumber(phoneNumber);
	assertEquals("1234567890", result);
}

@Test
void testSanitizePhoneNumberWithNoDashOrParenthesis() {
	String phoneNumber = "1234567890";
	String result = VendorHelper.sanitizePhoneNumber(phoneNumber);
	assertEquals("1234567890", result);
}

@Test
void testIsValidEmail() {
	String email = "abc@email.com";
	boolean result = VendorHelper.isValidEmail(email);
	assertTrue(result);
}

@Test
void testIsValidEmailWithNoAtSymbol() {
	String email = "abcemail.com";
	boolean result = VendorHelper.isValidEmail(email);
	assertFalse(result);
}
}
