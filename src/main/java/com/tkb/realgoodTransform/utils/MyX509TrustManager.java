package com.tkb.realgoodTransform.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.springframework.stereotype.Component;
@Component
public class MyX509TrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// 檢查客戶端證書是否可信
		// 如果可信，不抛出任何異常；否則，抛出CertificateException異常

	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// 檢查服務器端證書是否可信
		// 如果可信，不抛出任何異常；否則，抛出CertificateException異常

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// 返回一個可信的 CA 證書列表，如果没有，返回null
		// 此直接返回一個陣列
		return new X509Certificate[0];
	}

}