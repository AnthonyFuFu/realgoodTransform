package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

/**
 * 考取金榜內容
 */
@Data
public class AdmitContent {
	
	private Integer id;					//流水號
	private Integer admit_id;			//金榜流水號
	private String title;				//標題
	private String summary;				//簡介
	private String create_by;			//創建者
	private Timestamp create_date;		//創建時間
	
	private List<AdmitContentOption> admitOptionList;
	
}
