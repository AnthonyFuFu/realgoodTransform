package com.tkb.realgoodTransform.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

/**
 *  一點通高手經驗談model屬性
 */
@Data
public class GSATNews {

	private Integer 	id;					//流水號
	private String 		category;	  		//類別
	private String 		title;     			//標題名稱
	private String 		create_by;			//創建者
	private Timestamp 	create_date;		//創建日期
	private String 		update_by;			//編輯者
	private Date 		update_date;
	private String 		content;           //內容
	private Integer 	show;              //是否顯示於前台
	private Date 		begin_date;        //開始日期
	private Date 		end_date;          //結束日期
	
}