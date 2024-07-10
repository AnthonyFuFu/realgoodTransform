package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;

/**
 * 精選文章
 * @author 
 * @version 創建時間：2016-07-05
 */
@Data
public class ChosenArticle {
	
	private Integer id;						//流水號
	private String article_num;				//文章編號
	private Integer article_category;		//文章分類
	private String article_author;			//文章作者
	private String title;					//標題
	private String summary;					//簡介
	private String quote_content_string;	//從文章系統引用來的內容轉成字串型態
	private String content_string;			//內容轉成字串型態
	private String image_url;				//圖片URL
	private Integer click_rate;				//點閱率
	private String top_status;				//是否置頂(Y:是/N:否)
	private String display;					//顯示狀態
	private String delete_status;			//是否刪除
	private String create_by;				//創建者
	private String create_date;			//創建時間
	private String update_by;				//編輯者
	private Date update_date;			//編輯時間
	private Date last_update;			//最後修改時間(文章系統的部分)
	private String banner_url;				//BANNER URL
	private String edit_from;				//編輯學堂 
	private String content;					//從文章系統引用來的內容
	private String quote_content;				//內容
	private String encrypturl;             //內頁URL
	private String product_category;	//產品網址分類
	//暫存
	private String encrypt_id;				//加密id字串
	
	private String category_name;			//分類中文
	private String parent_category_name;	//父分類中文
	private String category_parent;			//父分類
	
	private Integer category_layer;			//分類級別
	
	private String show_message;			//頁面訊息
	
	private ChosenArticle prev_article;		//上一篇
	private ChosenArticle next_article;		//下一篇
	private Integer rownum;					//自然編號
	
	
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
