package com.tkb.realgoodTransform.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Marquee {
	
	private Integer id;								//流水號
	private String title;                			//標題
	private String content;                			//內容
	private String link;                			//網址
	private Integer sort;                			//排序
	private Date begin_date;						//開始日期
	private Date end_date;							//結束日期
	private String create_by;						//創建者
	private Timestamp create_date;					//創建時間
	private String update_by;						//編輯者
	private Timestamp update_date;					//編輯時間
	private String Show_message;					//訊息
	private Integer click_rate;						//點擊率
	
}
