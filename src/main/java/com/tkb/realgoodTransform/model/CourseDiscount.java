package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;

@Data
public class CourseDiscount {
	private Integer id;					//流水號
	private String title;				//標題
	private String category;			//類別
	private String image;				//圖片
	private Date begin_date;			//開始日期
	private Date end_date;				//結束日期
	private Integer click_rate;			//點擊率
	private String create_by;			//創建者
	private Date create_date;		//創建時間
	private String update_by;			//編輯者
	private String update_date;		//編輯時間
	private String encryptId;            //類別
	
	private Integer area_id;			//地區ID
	private String area_name;			//地區中文
	private String category_name;		//類別中文
	private Integer rownum;				//自然編號
	private String random;				//是否亂數排序
	private CourseDiscount prev_courseDiscount;				//上一篇
	private CourseDiscount next_courseDiscount;				//下一篇
	
	private String area;				
	private String content;				//內容(暫時)
	
	private String photo;				//列表圖片
	
	private String type_icon;			//列表類別圖片
	private String type_icon_text;		//列表類別圖片文字
	
	private Integer show;				//是否顯示於前台
	
	private String product_category;		//產品網址分類
	private String exam_analysis;		//跨考分析
	private String exam_analysis_link;	//跨考分析連結
	
	private String goodies;				//超值優惠
	private String gifts;				//嚴選好禮
	
	private Integer course_discount_top;	//首頁置頂
	private String top_count;			//置頂次數
	
	private String index_image;			//首頁圖片
	private Integer index_sort;			//首頁排序
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
	
	private String show_message;
	private String edm_type_id;         //EDM資料夾編號
	private String edm_url;             //EDM網址
}
