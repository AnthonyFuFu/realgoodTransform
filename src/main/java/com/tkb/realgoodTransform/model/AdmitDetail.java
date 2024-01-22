package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 考取金榜內容
 */
@Data
public class AdmitDetail {
	
	private Integer id;					//流水號
	private Integer admit_id;			//金榜流水號
	private String name;				//姓名
	private String type;				//考取類別
	private String achievement;			//名次
	private String create_by;			//創建者
	private Timestamp create_date;		//創建時間
	private String update_by;			//編輯者
	private Timestamp update_date;		//編輯時間
	
}
