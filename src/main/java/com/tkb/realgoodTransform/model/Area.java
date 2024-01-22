package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 地區
 * @author Ken
 * @version 創建時間：2016-04-28
 */
@Getter
@Setter
@ToString
public class Area {
	
	private Integer id;							//流水號
	private Integer parent_id;					//父層ID
	private String name;						//名稱
	private String layer;						//層級
	private String create_by;					//創建者
	private Timestamp create_date;				//創建時間
	private String update_by;					//編輯者
	private Timestamp update_date;				//編輯時間
	private String status; 						//狀態
	
	private String type;
	
	private String parent_name;					//父層名稱
	
	private List<Area> areaInnerList;			//第二層清單
	private List<Location> locationInnerList;	// 第二層清單
	
	private String mapUrl;						//地區地址

}
