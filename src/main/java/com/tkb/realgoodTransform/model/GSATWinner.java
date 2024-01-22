package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 *  一點通經驗談model屬性
 */
@Data
public class GSATWinner {

	private Integer id;					//流水號
	private String name;				//學生名稱
	private String title;      			//標題名稱
	private String create_by;			//創建者
	private Timestamp create_date;		//創建日期
	private String update_by;			//編輯者
	private Timestamp update_date;		//編輯日期
	private Integer sort;               //排序
	private String content;             //內容
	private Integer show;              	//是否顯示於前台
	private String front_url;          	//前台網址
	private Integer winner_id;
	
	private Integer pageCount;    	     //每頁筆數
	private Integer pageTotalCount;     //總筆數
	private Integer totalPage;          //總頁數
	private Integer leftStartPage;     //左邊開始頁碼
	private Integer leftEndPage;        //左邊結束頁碼
	private Integer rightStartPage;     //右邊開始頁碼
	private Integer rightEndPage;       //右邊結束頁碼
	private Integer leftPageNum;        //左邊頁碼數量
	private Integer rightPageNum;       //右邊頁碼數量
	
}