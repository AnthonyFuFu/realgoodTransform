package com.tkb.realgoodTransform.utils.ec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DES  {

	private Cipher dCipher;

	private SecretKey desKey;

	private Cipher eCipher;
	
	
	public DES(String password, String modeAndPadding) {
		if (password == null || password.trim().length() == 0)
			throw new IllegalArgumentException("Argument password can not be null or empty.");
		if (modeAndPadding == null || modeAndPadding.trim().length() == 0)
			throw new IllegalArgumentException("Argument modeAndPadding can not be null or empty.");
		try {
			this.desKey = generateKey(password);
			this.eCipher = Cipher.getInstance(modeAndPadding);
			this.dCipher = Cipher.getInstance(modeAndPadding);
			this.eCipher.init(1, this.desKey);
			this.dCipher.init(2, this.desKey);
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace(System.err);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace(System.err);
		} catch (InvalidKeyException ike) {
			ike.printStackTrace(System.err);
		}
	}
	

	public byte[] encrypt(byte[] data) {
		byte[] rtnValue = null;
		try {
			CipherInputStream cin = new CipherInputStream(new ByteArrayInputStream(data), this.eCipher);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buffer = new byte[64];
			int bytesRead;
			while ((bytesRead = cin.read(buffer)) != -1)
				bout.write(buffer, 0, bytesRead);
			bout.close();
			cin.close();
			rtnValue = bout.toByteArray();
		} catch (IOException ioe) {
			System.err.println("IOException occurred in [DES - encrypt()].");
			ioe.printStackTrace(System.err);
		}
		return rtnValue;
	}

	public String decrypt(byte[] data) {
		String rtnValue = null;
		try {
			ByteArrayInputStream bin = new ByteArrayInputStream(data);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			CipherOutputStream cout = new CipherOutputStream(bout, this.dCipher);
			byte[] buffer = new byte[64];
			int bytesRead;
			while ((bytesRead = bin.read(buffer)) != -1)
				cout.write(buffer, 0, bytesRead);
			cout.close();
			bout.close();
			bin.close();
			rtnValue = bout.toString();
		} catch (IOException ioe) {
			System.err.println("IOException occurred in [DES - decrypt()].");
			ioe.printStackTrace(System.err);
		}
		return rtnValue;
	}

	public SecretKey generateKey(String password) {
		SecretKey rtnValue = null;
		try {
			byte[] desKeyData = password.getBytes();
			DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey desKey = keyFactory.generateSecret(desKeySpec);
			rtnValue = desKey;
		} catch (InvalidKeyException ike) {
			ike.printStackTrace(System.err);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace(System.err);
		} catch (InvalidKeySpecException ikse) {
			ikse.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return rtnValue;
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}
}
