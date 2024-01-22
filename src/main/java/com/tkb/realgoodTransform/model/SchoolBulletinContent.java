package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 學堂公告內容
 */
@Data
public class SchoolBulletinContent {
	
	private Integer id;							//流水號
	private Integer school_bulletin_id;			//學堂公告ID
	private String icon;						//ICON
	private String title;						//標題
	private String content;						//內容
	private String create_by;					//創建者
	private Timestamp create_date;				//創建時間
	private String update_by;					//編輯者
	private Timestamp update_date;				//編輯時間
	
}
