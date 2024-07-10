package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;

@Data
public class NewExamContent {
	private Integer id;							//流水號
	private Integer newExam_id;			//熱門講座ID
	private String icon;						//ICON
	private String title;						//標題
	private String content;						//內容
	private String create_by;					//創建者
	private Date create_date;				//創建時間
	private String update_by;					//編輯者
	private Date update_date;				//編輯時間	
}
