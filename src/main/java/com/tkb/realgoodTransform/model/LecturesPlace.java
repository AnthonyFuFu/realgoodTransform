package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;

@Data
public class LecturesPlace {
	private Integer id;							//流水號
	private Integer lectures_id;				//熱門講座ID
	private String placeName;					//講座地點
	private String placeEvent;					//講座場次
	private String placeDay;					//講座時間
	private String create_by;					//創建者
	private Date create_date;					//創建時間
	private String update_by;					//編輯者
	private Date update_date;					//編輯時間	
	private String place_address;
	private String week;						//星期
	private String url;
	private String area_id; 					// 地區id
}
