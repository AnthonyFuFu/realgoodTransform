package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;

@Data
public class AdmitCategory {

	private Integer id;					//流水號
	private Integer parent_id;			//父層ID
	private String name;				//名稱
	private String layer;				//層級
	private String create_by;			//創建者
	private Date create_date;		//創建日期
	private String update_by;			//編輯者
	private Date update_date;		//編輯日期
	private Integer sort;				//排序
	
	private String parent_name;			//父層名稱	
	//暫存
	private Integer use_count;			//分類被使用的次數

	
	
	
}
