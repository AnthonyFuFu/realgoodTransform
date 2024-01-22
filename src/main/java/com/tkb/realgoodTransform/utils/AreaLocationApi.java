package com.tkb.realgoodTransform.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Location;
@Component
public class AreaLocationApi {

	// 獲取TKB_WEB專案的Area
	public String getApiArea() {
		try {
			URL url = new URL("https://www.tkb.com.tw/areaLocation/api/getArea");
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// 設置 HTTPS 連接的 SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new MyX509TrustManager() }, new SecureRandom());
			connection.setSSLSocketFactory(sslContext.getSocketFactory());
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestMethod("POST");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			connection.disconnect();
			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 獲取TKB_WEB專案的Website
	public String getApiWebsite() {
		try {
			URL url = new URL("https://www.tkb.com.tw/areaLocation/api/getWebsite");
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// 設置 HTTPS 連接的 SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new MyX509TrustManager() }, new SecureRandom());
			connection.setSSLSocketFactory(sslContext.getSocketFactory());
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestMethod("POST");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			connection.disconnect();
			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 獲取TKB_WEB專案的Location
	public String getApiLocation(String website, String area_id) {
		try {
			String apiUrl = "https://www.tkb.com.tw/areaLocation/api/getLocation";
			String param = "?website=" + URLEncoder.encode(website, "UTF-8") + "&area_id=" + URLEncoder.encode(area_id, "UTF-8");
			URL url = new URL(apiUrl + param);

			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// 設置 HTTPS 連接的 SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new MyX509TrustManager() }, new SecureRandom());
			connection.setSSLSocketFactory(sslContext.getSocketFactory());
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestMethod("POST");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			connection.disconnect();
			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// 將Json轉成List<?>格式
	public List<Area> jsonToAreaList(String json) {
		Gson gson = new Gson();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		List<Area> areaList = new ArrayList<>();
		if (jsonElement.isJsonArray()) {
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (JsonElement element : jsonArray) {
				if (element.isJsonObject()) {
					JsonObject jsonObject = element.getAsJsonObject();
					JsonObject convertedJsonObject = convertKeysToLowercase(jsonObject);
					Area area = gson.fromJson(convertedJsonObject, Area.class);
					areaList.add(area);
				}
			}
		}
		return areaList;
	}

	// 將Json轉成List<?>格式
	public List<Location> jsonToLocationList(String json) {
		GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.setDateFormat("yyyy-MM-dd");
	    Gson gson = gsonBuilder.create();
		JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
		List<Location> locationList = new ArrayList<>();
		if (jsonElement.isJsonArray()) {
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (JsonElement element : jsonArray) {
				if (element.isJsonObject()) {
					JsonObject jsonObject = element.getAsJsonObject();
					JsonObject convertedJsonObject = convertKeysToLowercase(jsonObject);
					Location location = gson.fromJson(convertedJsonObject, Location.class);
					locationList.add(location);
				}
			}
		}
		return locationList;
	}

	// 將Key轉成小寫
	public JsonObject convertKeysToLowercase(JsonObject jsonObject) {
		JsonObject convertedJsonObject = new JsonObject();
		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			String lowercaseKey = entry.getKey().toLowerCase();
			JsonElement value = entry.getValue();
			convertedJsonObject.add(lowercaseKey, value);
		}
		return convertedJsonObject;
	}

}
