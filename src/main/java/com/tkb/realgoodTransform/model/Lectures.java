package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;

@Data
public class Lectures {
	private Integer id;					//流水號
	private String title;				//標題
	private String area;				//地區
	private String category;			//類別
	private String image;				//圖片
	private String photo;               //首頁圖片
	private Date begin_date;			//開始日期
	private Date end_date;				//結束日期
	private Integer click_rate;			//點擊率
	private String create_by;			//創建者
	private Date create_date;		//創建時間
	private String update_by;			//編輯者
	private Date update_date;		//編輯時間
	private String fare;				//票價
	private String place;				//地點
	private String phone;				//電話
	private String fare_money;			//票價金額
	private String lectures_top;		//至頂
	private String top_count;			//置頂次數
	private String seo_content;			//seo描述
	private Integer lecture_type_id;	//講座編號
	private String encryptId;			//加密ID
	private String encrypturl;			//加密ID
	private String short_title;         //列表短標
	private Integer parent_id;           //第一層類別id    
	private String product_category;	//產品網址分類
	
	private Integer area_id;			//地區ID
	private String area_name;			//地區中文
	private String category_name;		//類別中文
	private Integer rownum;				//自然編號
	private String random;				//是否亂數排序
	private Lectures prev_Lectures;				//上一篇
	private Lectures next_Lectures;				//下一篇

	private String content;				//內容(暫時)
	
	private String date;
	private String week_begin_date;
	private String week_end_date;
	
	private Integer pageCount;			//每頁筆數
	private Integer pageTotalCount;		//總筆數
	private Integer totalPage;			//總頁數
	
	private Integer leftStartPage;		//左邊開始頁碼
	private Integer leftEndPage;		//左邊結束頁碼
	private Integer rightStartPage;		//右邊開始頁碼
	private Integer rightEndPage;		//右邊結束頁碼
	private Integer leftPageNum;		//左邊頁碼數量
	private Integer rightPageNum;		//右邊頁碼數量
	
}
