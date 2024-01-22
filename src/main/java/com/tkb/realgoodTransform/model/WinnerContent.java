package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class WinnerContent {
	
	private Integer id;							//流水號
	private Integer winner_id;					//贏家經驗談ID
	private String icon;						//ICON
	private String title;						//標題
	private String content;						//內容
	private String create_by;					//創建者
	private Timestamp create_date;				//創建時間
	private String update_by;					//編輯者
	private Timestamp update_date;				//編輯時間
	
}
