package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 *  一點通高手經驗談model屬性
 */
@Data
public class GSATAdmit {

	private Integer 	id;					//流水號
	private String 		seo;				//學生名稱
	private String 		title;      		//標題名稱
	private String 		content;    		//簡介
	private Integer 	sort;               //排序
	private Integer 	show;              	//是否顯示於前台
	private String 		create_by;			//創建者
	private Timestamp 	create_date;		//創建日期
	private String 		update_by;			//編輯者
	private Timestamp 	update_date;		//編輯日期
	
}
