package com.tkb.realgoodTransform.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 基本控制類
 * @author Joshua
 * @version 創建時間2022-01-25
 */
@Component
public class BaseUtils {
	
	protected int pageCount = 10;				//每頁筆數
	protected int pageTotalCount;				//總筆數
	protected int totalPage;					//總頁數
	protected int pageStart;					//起始筆數
	
	protected int leftStartPage;				//左邊開始頁碼
	protected int leftEndPage;					//左邊結束頁碼
	protected int rightStartPage;				//右邊開始頁碼
	protected int rightEndPage;					//右邊結束頁碼
	protected int pageMaxNum = 5;				//只顯示的頁碼數量
	protected int leftPageNum;					//左邊頁碼數量
	protected int rightPageNum;					//右邊頁碼數量
	
	protected final String url = "https://www.google.com/recaptcha/api/siteverify";
	protected final String secret = "6Lca5J8UAAAAAJNUEh68CrDu1SGEXLUbmC-E6dFy";
	
	protected String articleAppName = "ARTICLE";				//APP名字
	protected String articleReqEncryptKey = "article-req-xxxx";	//接收端鑰匙
	protected String articleRepDecryptKey = "article-rep-xxxx";	//傳送端鑰匙

	public int pageSetting(int pageNo) {
		if (pageTotalCount % pageCount == 0) {
			totalPage = pageTotalCount / pageCount;
		} else {
			totalPage = (pageTotalCount / pageCount) + 1;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		} else if (pageNo > totalPage) {
			pageNo = totalPage;
		}
		pageStart = ((pageNo-1) * pageCount) < 0 ? 0 : ((pageNo-1) * pageCount);
		
		//左右邊頁碼起訖初始化
		leftStartPage = 1;
		leftEndPage = pageNo - 1;
		rightStartPage = pageNo + 1;
		rightEndPage = totalPage;
		
		//
		if(totalPage < pageMaxNum) {
			pageMaxNum = totalPage;
		}
		leftPageNum = 0;
		rightPageNum = pageMaxNum - 1;
		//當頁左右頁碼數量判斷
		if(pageNo > 1) {
			if(pageNo <= pageMaxNum/2) {
				leftPageNum = pageNo - 1;
				rightPageNum = pageMaxNum - pageNo;
			} else if((pageMaxNum%2 == 0 && totalPage-pageNo <= pageMaxNum/2-1) || (pageMaxNum%2 == 1 && totalPage-pageNo <= pageMaxNum/2)) {
				rightPageNum = totalPage - pageNo;
				leftPageNum = pageMaxNum-rightPageNum-1;
			} else{
				leftPageNum = pageMaxNum / 2;
				rightPageNum = (pageMaxNum%2 == 0) ? pageMaxNum/2-1 : pageMaxNum/2 ;
			}
		}
		
		//當頁左右頁碼起訖設定
		leftStartPage = pageNo - leftPageNum;
		leftEndPage = pageNo - 1;
		rightStartPage = pageNo + 1;
		rightEndPage = pageNo + rightPageNum;
		
		return pageNo;
	}
	
	//2022/02/17新增，回去index方法要先reset分頁的值
	public void resetPage() {
		pageTotalCount = 0;
		pageStart = 0;
		leftStartPage = 0;
		leftEndPage = 0;
		rightStartPage = 0;
		rightEndPage = 0;
		leftPageNum = 0;
		rightPageNum = 0;
		pageCount = 10;
		pageMaxNum = 5;
	}
	
	//2022/03/28進入頁面的頁碼設定
	public int setPage(int pageNo, HttpServletRequest request) {
		if(pageNo != 0) {
			pageNo = request.getParameter("pageNo") == null ? 0 : Integer.valueOf(request.getParameter("pageNo"));
		}
		totalPage = request.getParameter("totalPage") == null ? 0 : Integer.valueOf(request.getParameter("totalPage"));
		resetPage();
		return pageNo;
	}
	
	// 2022/03/28將頁碼參數加入model
	public Model addModelAttribute(int pageNo, Model model) {
		model.addAttribute("pageNo", pageNo).addAttribute("pageTotalCount", pageTotalCount)
				.addAttribute("pageCount", pageCount).addAttribute("totalPage", totalPage)
				.addAttribute("leftStartPage", leftStartPage).addAttribute("leftPageNum", leftPageNum)
				.addAttribute("rightPageNum", rightPageNum).addAttribute("leftEndPage", leftEndPage)
				.addAttribute("rightStartPage", rightStartPage).addAttribute("rightEndPage", rightEndPage);
		return model;
	}

	//2022/03/08加入，圖片寬判斷
	public boolean checkImageWidth(MultipartFile image,int maxWidth) throws IOException {
		BufferedImage buff = ImageIO.read(image.getInputStream());
		if(buff.getWidth() == maxWidth){
			return true;
		}else{
			return false;
		}		
	}
	//2022/03/08加入，圖片長判斷
	public boolean checkImageHeight(MultipartFile image,int maxHeight) throws IOException {
		BufferedImage buff = ImageIO.read(image.getInputStream());
		if(buff.getHeight() == maxHeight){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	/**
	 * google recaptcha接收驗證結果
	 * 
	 * @param gRecaptchaResponse
	 * @param userAgent
	 * @return
	 * @throws IOException
	 */
	public boolean verify(String gRecaptchaResponse, String userAgent) throws IOException {

		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}

		try {
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", userAgent);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String postParams = "secret=" + secret + "&response=" + gRecaptchaResponse;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
//    System.out.println("\nSending 'POST' request to URL : " + url);
//    System.out.println("Post parameters : " + postParams);
//    System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
//    System.out.println(response.toString());

			// parse JSON response and return 'success' value
			JSONObject jsonObj = new JSONObject(response.toString());
			String status = jsonObj.get("success").toString();

			if (status.equals("false")) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
