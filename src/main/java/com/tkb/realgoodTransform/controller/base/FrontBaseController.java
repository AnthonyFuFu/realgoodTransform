package com.tkb.realgoodTransform.controller.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

public class FrontBaseController {

	public Pattern pattern;
	public Matcher matcher;
	
	public boolean checkEmail(String patterned) {

		String  emailPattern ="\\w+@\\w+\\.\\w";

		pattern = Pattern.compile(emailPattern);
		matcher = pattern.matcher(patterned);

		return matcher.matches();
	}

	public boolean checkPhone(String patterned) {
		String phonePattern = "^[09]{2}[0-9]{8}$";

		pattern = Pattern.compile(phonePattern);
		matcher = pattern.matcher(patterned);

		return matcher.matches();
	}
	
	//驗證帳號長度
	public boolean checkMemberPasswordLength(String patterned) {
		boolean length = false;

		if(patterned.length() >= 8 && patterned.length() <=12){
			length = true;
		}

		return length;
	}
	
	//驗證帳號長度
	public boolean checkMemberPassword(String patterned) {
		String passwordPattern = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$^&*_]).{8,12}";

		pattern = Pattern.compile(passwordPattern);
		matcher = pattern.matcher(patterned);

		return matcher.matches();
	}
	
	//取得client ip 位址
  	public static String getClientIpAddr(HttpServletRequest request) {
	  	String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		    ip = request.getRemoteAddr();
		}
		return ip;
  	}
  	
 // 判斷瀏覽裝置
   	public String getUserAgent(HttpServletRequest request) {
 		String ua = request.getHeader("user-agent");
 		String login_equipment = "";
 		if (ua.indexOf("Android") > -1 || ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1) {
 			login_equipment = "MOBILE";
 		} else {
 			login_equipment = "COM";
 		}

 		return login_equipment;
 	}
}
