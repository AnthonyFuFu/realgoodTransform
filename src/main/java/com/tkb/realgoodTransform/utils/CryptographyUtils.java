package com.tkb.realgoodTransform.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptographyUtils {
	protected static String passwordKey = "LEARNKEY";
	//回傳加密字串
	public static String encryptStr(String inputWord) {
			
		String encryptResult = encrypt(inputWord, passwordKey);
		return 	encryptResult;
		
	}
	
	//回傳解密字串
	public static String  staticdecryptStr(String inputWord)throws UnsupportedEncodingException {
			
		String decryptResult = decrypt(inputWord, passwordKey);
		return 	decryptResult;
		
	}
	/** 
     * 加密 
     */  
    public static String encrypt(String content, String key) {  
        try {  
        	
        	 KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        	 SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");   
             secureRandom.setSeed(key.getBytes());   
             kgen.init(128, secureRandom);
             SecretKey secretKey = kgen.generateKey();  
             byte[] enCodeFormat = secretKey.getEncoded();  
             SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");  
             Cipher cipher = Cipher.getInstance("AES");  
             byte[] byteContent = content.getBytes("utf-8");  
             cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);  
             byte[] byteRresult = cipher.doFinal(byteContent);  
             StringBuffer sb = new StringBuffer();  
             for (int i = 0; i < byteRresult.length; i++) {  
                 String hex = Integer.toHexString(byteRresult[i] & 0xFF);  
                 if (hex.length() == 1) {  
                     hex = '0' + hex;  
                 }  
                 sb.append(hex.toUpperCase());  
             }  
             return sb.toString();  
//            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
//            kgen.init(128, new SecureRandom(key.getBytes()));  
//            SecretKey secretKey = kgen.generateKey();  
//            byte[] enCodeFormat = secretKey.getEncoded();  
//            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");  
//            Cipher cipher = Cipher.getInstance("AES");  
//            byte[] byteContent = content.getBytes("utf-8");  
//            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);  
//            byte[] byteRresult = cipher.doFinal(byteContent);  
//            StringBuffer sb = new StringBuffer();  
//            for (int i = 0; i < byteRresult.length; i++) {  
//                String hex = Integer.toHexString(byteRresult[i] & 0xFF);  
//                if (hex.length() == 1) {  
//                    hex = '0' + hex;  
//                }  
//                sb.append(hex.toUpperCase());  
//            }  
//            return sb.toString();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }
    
    /** 
     * 解密 
     */  
    public static String decrypt(String content, String key)throws UnsupportedEncodingException {  
        if (content.length() < 1)  
            return null;  
        byte[] byteRresult = new byte[content.length() / 2];  
        for (int i = 0; i < content.length() / 2; i++) {  
            int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);  
            int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);  
            byteRresult[i] = (byte) (high * 16 + low);  
        }  
        try {
        	KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        	SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");   
            secureRandom.setSeed(key.getBytes());   
            kgen.init(128, secureRandom);  
            SecretKey secretKey = kgen.generateKey();  
            byte[] enCodeFormat = secretKey.getEncoded();  
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");  
            Cipher cipher = Cipher.getInstance("AES");  
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);  
            byte[] result = cipher.doFinal(byteRresult);
            return new String(result, "UTF-8");
//            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
//            kgen.init(128, new SecureRandom(key.getBytes()));  
//            SecretKey secretKey = kgen.generateKey();  
//            byte[] enCodeFormat = secretKey.getEncoded();  
//            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");  
//            Cipher cipher = Cipher.getInstance("AES");  
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);  
//            byte[] result = cipher.doFinal(byteRresult);
//            return new String(result, "UTF-8");
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }

}
