package com.tkb.realgoodTransform.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

/**
 *  一點通文章model屬性
 */
@Data
public class GSAT {

	private Integer id;						//流水號
	private String category;				//考試類別名稱
	private String title;					//標題(頁籤)名稱
	private String seo;						//SEO網頁描述
	private String article_name;			//考試文章名稱
	private String article_url;				//考試文章連結
	private String content;					//內容
	private Date begin_date;				//開始日期
	private Date end_date;					//結束日期
	private Integer click_rate;				//點擊率	
	private String create_by;				//創建者
	private Timestamp create_date;			//創建時間
	private String update_by;				//編輯者
	private Timestamp update_date;			//編輯時間	
	private Integer sort;               	//排序
	private Integer show;              		//是否顯示於前台
	
	private String encrypt_id;             	//加密id字串
	private String front_url;				//前台網址
	
	private String category_name;			//類別中文
	
	private Integer rownum;					//自然編號
	private String random;					//是否亂數排序
	
	private String date;
	private String week_begin_date;
	private String week_end_date;
	
	private Integer pageCount;				//每頁筆數
	private Integer pageTotalCount;			//總筆數
	private Integer totalPage;				//總頁數
	
	private Integer leftStartPage;			//左邊開始頁碼
	private Integer leftEndPage;			//左邊結束頁碼
	private Integer rightStartPage;			//右邊開始頁碼
	private Integer rightEndPage;			//右邊結束頁碼
	private Integer leftPageNum;			//左邊頁碼數量
	private Integer rightPageNum;			//右邊頁碼數量	
	
}
