package com.tkb.realgoodTransform.fbApi;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.serverside.ActionSource;
import com.facebook.ads.sdk.serverside.Event;
import com.facebook.ads.sdk.serverside.EventRequest;
import com.facebook.ads.sdk.serverside.EventResponse;
import com.facebook.ads.sdk.serverside.UserData;
import com.tkb.realgoodTransform.controller.base.FrontBaseController;

import jakarta.servlet.http.HttpServletRequest;

@Controller

public class ConversionApi extends FrontBaseController {
	
	public static final String ACCESS_TOKEN = "EAAGIBp3FJwMBABRxb0qTluAXHtV9FV0YXVMhi5R4hICwMDU9lxNJ25BkcMVME8v1YxzrDb68JVxLJzDFlRrKUyBDUToqYtBSB7ZCrHzZAJ5ZAXUzVAcL2x2ZCxDFFkuZAYLjwfPSDKhFzpSDgZClx4pt3ldQgbbImLe5CNea7zaKnkf3aedpL9LNH6ZBZA3RgdAZD";
	public static final String PIXEL_ID = "892656040820119";
	
	// 送單
	public void lead(HttpServletRequest request, String email, String phone) {
		APIContext context = new APIContext(ACCESS_TOKEN).enableDebug(true);
		context.setLogger(System.out);
		List<Event> events = new ArrayList<>();
		UserData userData_0 = new UserData().emails(Arrays.asList(sha256(email))).phones(Arrays.asList(sha256(phone)))
				.clientIpAddress(getClientIpAddr(request)).clientUserAgent(getUserAgent(request));

		Event event_0 = new Event().eventName("Lead").eventTime(System.currentTimeMillis() / 1000L).userData(userData_0)
//					    	      .customData(customData_0)
				.actionSource(ActionSource.website);
		events.add(event_0);

		EventRequest eventRequest = new EventRequest(PIXEL_ID, context).data(events);

		try {
			EventResponse response = eventRequest.execute();
			System.out.printf("Standard API response : %s ", response);
		} catch (APIException e) {
			e.printStackTrace();
		}
	}

	// 瀏覽頁面
//	@RequestMapping("/apiPage/view")
	public void viewContent(HttpServletRequest request) {
		APIContext context = new APIContext(ACCESS_TOKEN).enableDebug(true);
		context.setLogger(System.out);
		List<Event> events = new ArrayList<>();

		UserData userData_0 = new UserData().clientIpAddress(getClientIpAddr(request))
				.clientUserAgent(getUserAgent(request));

		Event event_0 = new Event().eventName("ViewContent").eventTime(System.currentTimeMillis() / 1000L)
				.userData(userData_0)
//					    	      .customData(customData_0)
				.actionSource(ActionSource.website);
		events.add(event_0);

		EventRequest eventRequest = new EventRequest(PIXEL_ID, context).data(events);

		try {
			EventResponse response = eventRequest.execute();
			System.out.printf("Standard API response : %s ", response);
		} catch (APIException e) {
			e.printStackTrace();
		}
	}

	// 字串轉SHA-256
	public String sha256(String str) {
		final StringBuilder hexString = new StringBuilder();

		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA-256");
			final byte[] hash = digest.digest(str.getBytes("UTF-8"));
			for (int i = 0; i < hash.length; i++) {
				final String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return hexString.toString();
	}

}
