package com.rimalholdings.expensemanager.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VendorHelper {
  public static String generateVendorId(String name) {
  String[] nameParts = name.split(" ");
  StringBuilder vendorId = new StringBuilder();
  if (nameParts.length >= 2) {
    vendorId.append(nameParts[0].charAt(0));
    vendorId.append(nameParts[1]);
  }
  return vendorId.toString().toLowerCase();
}

public static String sanitizePhoneNumber(String phoneNumber) {
  String sanitizedNumber = phoneNumber.replaceAll("[-()]", "");
  return sanitizedNumber.length() > 10 ? sanitizedNumber.substring(0, 10) : sanitizedNumber;
}
public static boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
}

}
