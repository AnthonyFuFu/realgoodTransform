package com.tkb.realgoodTransform.utils.ec;

public class Hex {
	 private static final char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
	   'd', 'e', 'f' };

	 public static String hexEncode(byte[] bytes) {
	  StringBuffer s = new StringBuffer(bytes.length * 2);
	  for (int i = 0; i < bytes.length; i++) {
	   byte b = bytes[i];
	   s.append(digits[(b & 0xF0) >> 4]);
	   s.append(digits[b & 0xF]);
	  }
	  return s.toString();
	 }

	 public static byte[] hexDecode(String s) throws IllegalArgumentException {
	  try {
	   int len = s.length();
	   byte[] r = new byte[len / 2];
	   for (int i = 0; i < r.length; i++) {
	    int digit1 = s.charAt(i * 2);
	    int digit2 = s.charAt(i * 2 + 1);
	    if (digit1 >= 48 && digit1 <= 57) {
	     digit1 -= 48;
	    } else if (digit1 >= 97 && digit1 <= 102) {
	     digit1 -= 87;
	    }
	    if (digit2 >= 48 && digit2 <= 57) {
	     digit2 -= 48;
	    } else if (digit2 >= 97 && digit2 <= 102) {
	     digit2 -= 87;
	    }
	    r[i] = (byte) ((digit1 << 4) + digit2);
	   }
	   return r;
	  } catch (Exception e) {
	   throw new IllegalArgumentException("hexDecode(): invalid input");
	  }
	 }

	 public static void main(String[] args) {
	  String s = "687474703a2f2f64656d6f2e746b622e636f6d2e74773a383038302f77696e5f64656d6f2f";
	  byte[] dec = hexDecode(s);
	  System.out.println("" + new String(dec));
	 }
	}