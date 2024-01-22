package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 考取金榜Log
 */
@Data
public class AdmitLog {
	
	private Integer id;					//流水號
	private Integer admit_id;			//金榜流水號
	private String action_type;			//修改或刪除(1:修改,2:刪除)
	private String title;				//標題
	private String create_by;			//創建者
	private Timestamp create_date;		//創建時間
	
}
