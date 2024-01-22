package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 贏家經驗談內容
 */
@Data
public class GSATWinnerContent {

	private Integer 	id;						//流水號
	private Integer 	winner_id;				//贏家經驗談ID
	private String 		title;					//標題
	private String 		content;				//內容
	private String 		create_by;				//創建者
	private Timestamp 	create_date;			//創建時間
	private String 		update_by;				//編輯者
	private Timestamp 	update_date;			//編輯時間
	
}