package com.tkb.realgoodTransform.model;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class WinnerCategory {
	private Integer id;					//流水號
	private Integer parent_id;			//父層ID
	private String parent_name;			//父層名稱
	private String name;				//名稱
	private Integer category_code;       //類別編號
	private String layer;				//層級
	private String create_by;			//創建者
	private Date create_date;		//創建日期
	private String update_by;			//編輯者
	private Date update_date;		//編輯日期
	private List<WinnerCategory> winnerCategoryIIList;	//第二層清單
	private Integer sort;               //排序
	private Integer use_count;			//計算數量_父層
}
