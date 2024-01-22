package com.tkb.realgoodTransform.utils.ec;


public class DesEncrypt {
	public static String desEncrypt(String password, String desKey, String desModeAndPadding) {
		DES des = new DES(desKey, desModeAndPadding);
		byte[] encrypted = des.encrypt(password.getBytes());
		String hexedPwd = Hex.hexEncode(encrypted);
		return hexedPwd;
	}

	public static String desDecrypted(String password, String desKey, String desModeAndPadding) {
		DES des = new DES(desKey, desModeAndPadding);
		byte[] hexedPwd = Hex.hexDecode(password);
		String encrypted = des.decrypt(hexedPwd);
		return encrypted;
	}
}
