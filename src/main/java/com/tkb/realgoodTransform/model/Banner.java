package com.tkb.realgoodTransform.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

/**
 * 首頁Banner
 */
@Data
public class Banner {
	
	private Integer id;					//序號
	private String image;				//圖片
	private String image_rwd;			//手機板圖片
	private String title;				//標題
	private String link;				//檔案連結
	private Integer click_rate;			//點擊率
	private Date begin_date;			//開始日期
	private Date end_date;				//結束日期
	private String create_by;			//創建者
	private Timestamp create_date;		//創建時間
	private String update_by;			//編輯者
	private Timestamp update_date;		//編輯時間
	private Integer show;				//是否顯示於前台
	private Integer sort;				//排序
	private Integer count;				//計算數量
	
	private String show_message;		//頁面訊息
	
}
