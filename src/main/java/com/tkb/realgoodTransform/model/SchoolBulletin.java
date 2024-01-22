package com.tkb.realgoodTransform.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

/**
 * 學堂公告
 */
@Data
public class SchoolBulletin {
	
	private Integer id;								//流水號
	private String title;							//標題
	private String area;							//地區
	private String category;						//類別
	private String image;							//圖片
	private Date begin_date;						//開始日期
	private Date end_date;							//結束日期
	private Integer click_rate;						//點擊率
	private String create_by;						//創建者
	private Timestamp create_date;					//創建時間
	private String update_by;						//編輯者
	private Timestamp update_date;					//編輯時間
	
	private String account;							//帳號
	private Integer area_id;						//地區ID
	private String area_name;						//地區中文
	private String category_name;					//類別中文
	private Integer rownum;							//自然編號
	private String random;							//是否亂數排序
	private SchoolBulletin prev_school_bulletin;	//上一篇
	private SchoolBulletin next_school_bulletin;	//下一篇
	
	private String content;							//內容(暫時)
	
	private String date;
	private String week_begin_date;
	private String week_end_date;
	
	private Integer pageCount;						//每頁筆數
	private Integer pageTotalCount;					//總筆數
	private Integer totalPage;						//總頁數
	
	private Integer leftStartPage;					//左邊開始頁碼
	private Integer leftEndPage;					//左邊結束頁碼
	private Integer rightStartPage;					//右邊開始頁碼
	private Integer rightEndPage;					//右邊結束頁碼
	private Integer leftPageNum;					//左邊頁碼數量
	private Integer rightPageNum;					//右邊頁碼數量
	
	private String show_message;					//頁面訊息

	private String encrypt_id;						//加密id字串
	private String encrypturl;             			//內頁URL
	
}
