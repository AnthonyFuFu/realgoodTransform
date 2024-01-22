package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Edm {
	private Integer id;								//流水號
	private String name;							//姓名
	private String e_mail;							//電子郵件
	private String sale_agree;						//是否同意收到電子報，是：1，否：0
	private Integer web_location_id;				//地點
	private String ip;								//填表人的IP位置
	private Integer print_id;						//PO版位置流水號
	private String memo;							//備註
	private Timestamp create_date;					//創建時間
	private Integer type_id;						//EDM資料夾編號
	private String contact_time;					//聯絡時間
	private String type_code;						//關連類別代碼，輸入「EDM」
	private String is_read;							//是否看過個資法，是：1，否：0
	private String anatomic;						//單位網站代碼學堂：T / 大碩：D / 百官：A / 龍門：L / 甄戰：R / 洋碩：Y / 千碩：C / TV：V / GO：B
	private String login_equipment; 				//登入裝置CODE_DESCRION-LOGIN_EQUIPMENT
	private String is_del;							//0:未刪除  1:已刪除
	private String phone;							//手機
	private String web_location_code;				//地點代碼
	private String code;
	private Timestamp lectures_session_datetime;	//講座場次時間
	private String repeatID;	 					//重複名單
	private String keyword;							//google廣告參數
	private String network;							//google廣告參數
	private String device;							//google廣告參數
	private String campaignid;						//google廣告參數
	private String is_google_ad;					//google廣告參數
	
}
