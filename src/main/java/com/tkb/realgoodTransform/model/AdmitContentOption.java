package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 考取金榜
 */
@Data
public class AdmitContentOption {
	
	private Integer id;				//流水號
	private Integer content_id;		//金榜內容流水號
	private String achievement;		//名次
	private String name;			//姓名
	private String create_by;		//創建者
	private Timestamp create_date;	//創建時間
	
}
