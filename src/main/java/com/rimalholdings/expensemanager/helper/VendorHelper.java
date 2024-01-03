package com.rimalholdings.expensemanager.helper;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VendorHelper {
  private static final Integer VENDOR_ID_SEED = 10000;
  private static final Integer VENDOR_ID_MAX = 99999;
  public static String generateVendorId(String name,Integer zipCode) {
  String[] nameParts = name.split(" ");
  StringBuilder vendorId = new StringBuilder();
  if (nameParts.length >= 2) {
    vendorId.append(nameParts[0].charAt(0));
    vendorId.append(nameParts[1]);
    vendorId.append(zipCode);
  }
  return vendorId.toString().toLowerCase();
}
private static Integer randomInt() {
  Random random = new Random();
  return random.nextInt(VENDOR_ID_MAX - VENDOR_ID_SEED) + VENDOR_ID_SEED;

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
