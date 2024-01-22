package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 *  外特選單model屬性
 */
@Data
public class GSATMenu {

	private Integer id;					//流水號
	private Integer parent_id;			//父層ID
	private String name;				//名稱
	private String layer;				//層級
	private String link;				//連結
	private String create_by;			//創建者
	private Timestamp create_date;		//創建日期
	private String update_by;			//編輯者
	private Timestamp update_date;		//編輯日期
	private Integer sort;               //排序
	private String parent_name;			//父層名稱		
	private Integer show;				//是否顯示於前台
	
}
