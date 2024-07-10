package com.tkb.realgoodTransform.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Winner {

	private Integer id;								//流水號
	private Integer click_rate;						//點擊率
	private Integer show;							//是否顯示於前台
	private Integer show_index;						//是否顯示於首頁影音專區
	private Integer stick_top;						
	private String year;                			//年度
	private String category;						//類別
	private String parent_category;					//父類別
    private String category_three;					//第三層類別
	private String category_name;					//類別中文
	private String cate_name;            //類別名稱
	private String parent_name;						//父類別中文
	private String name;                			//姓名
	private String name_front;                		//前台姓名○○
	private String admitted;                		//考取學校
	private String original;                		//原本學校
	private String content;            			    //內容
	private String summary;            			    //心得簡介
	private String image;							//內頁圖片
	private String photo;							//人像圖片
	private String video;							//影音
	private String parent_id;						//父層ID
	private String video_image;                     ////首頁影音圖片
	private String encrypt_id;				//加密id字串
	private String encrypturl;				//加密ID
	private String product_category;	//產品網址分類
	private String create_by;						//創建者
	private String create_date;					//創建時間
	private String update_by;						//編輯者
	private Date update_date;					//編輯時間
	private Integer rownum;							//自然編號
	private String random;							//是否亂數排序
	private Winner prev_winner;	  	    			//上一篇
	private Winner next_winner;	  					//下一篇
	private Integer pageCount;						//每頁筆數
	private Integer pageTotalCount;					//總筆數
	private Integer totalPage;						//總頁數
	
	private Integer leftStartPage;					//左邊開始頁碼
	private Integer leftEndPage;					//左邊結束頁碼
	private Integer rightStartPage;					//右邊開始頁碼
	private Integer rightEndPage;					//右邊結束頁碼
	private Integer leftPageNum;					//左邊頁碼數量
	private Integer rightPageNum;					//右邊頁碼數量
	
	private Integer use_count;                      //category有被使用的id
	private Map prev_post;	  	    			//上一個
	private Map next_post;	  					//下一個
	
	private Integer prev_id;	  	    			//上一個
	private Integer next_id;	  					//下一個
	
	private String prev_name; //下一筆名子
	private String next_name; //下一筆名子
	private String prev_show;//上一筆顯示前台否
	private String next_show;//下一筆顯示前台否
	
	private String categoryName;
	private List<Winner> videoList;
	
}
