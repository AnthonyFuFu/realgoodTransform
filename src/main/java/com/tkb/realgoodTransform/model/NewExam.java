package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;

@Data
public class NewExam {
	private Integer id;					//流水號
	private String title;				//標題
	private String area;				//地區
	public String category;			//類別
	private String image;				//圖片
	private Date begin_date;			//開始日期
	private Date end_date;				//結束日期
	private Integer click_rate;			//點擊率
	private String create_by;			//創建者
	private Date create_date;		//創建時間
	private String update_by;			//編輯者
	private Date update_date;		//編輯時間
	private String encryptId;      		//加密ID
	private String encrypturl;			//加密ID
	private Integer index_sort;			//首頁排序   
	private String product_category;	//產品網址分類
	
	
	private Integer area_id;			//地區ID
	private String area_name;			//地區中文
	private String category_name;		//類別中文
	private Integer rownum;				//自然編號
	private String random;				//是否亂數排序
	private NewExam prev_newExam;				//上一篇
	private NewExam next_newExam;				//下一篇
	
	private String content;				//內容(暫時)
	
	private String photo;				//列表圖片
	
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
