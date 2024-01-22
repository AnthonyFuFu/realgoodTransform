package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class ChosenArticleCategory {

	private Integer id;								//流水號
	private Integer parent_id;						//父層ID
	private String name;							//名稱
	private String layer;							//層級
	private String create_by;						//創建者
	private Timestamp create_date;					//創建日期
	private String update_by;						//編輯者
	private Timestamp update_date;					//編輯日期
	private Integer sort;							//排序
	
	//暫存
	private String parent_name;						//父層名稱
	
	private Integer use_count;						//分類被使用的次數
	private Integer parent_use_count;				//分類被使用的次數
	private List<ChosenArticleCategory> childList;	//分類子類別清單
	private Integer in_use;							//使用中分類
	
}
