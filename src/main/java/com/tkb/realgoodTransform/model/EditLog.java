package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 所有功能編輯的LOG
 */
@Data
public class EditLog {
	
	private Integer id;					//LOG流水號
	private String function;			//功能
	private Integer data_id;			//功能流水號
	private String action_type;			//動作 ADD:新增,DELETE:刪除,UPDATE:更新
	private String content;				//內容(json格式)
	private String create_by;			//創建者
	private Timestamp create_date;		//創建時間
	
}
