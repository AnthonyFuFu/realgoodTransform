package com.tkb.realgoodTransform.model;

import java.sql.Clob;
import java.sql.Timestamp;

import lombok.Data;

/**
 * 精選文章
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
	private Timestamp create_date;			//創建時間
	private String update_by;				//編輯者
	private Timestamp update_date;			//編輯時間
	private Timestamp last_update;			//最後修改時間(文章系統的部分)
	private String banner_url;				//BANNER URL
	private String edit_from;				//編輯學堂 
	private Clob content;					//從文章系統引用來的內容
	private Clob quote_content;				//內容
	
	//暫存
	private String encrypt_id;				//加密id字串
	private String encrypturl;              //內頁URL
	private String category_name;			//分類中文
	private String parent_category_name;	//父分類中文
	private String category_parent;			//父分類
	
	private Integer category_layer;			//分類級別
	
	private String show_message;			//頁面訊息
	
	private ChosenArticle prev_article;		//上一篇
	private ChosenArticle next_article;		//下一篇
	private Integer rownum;					//自然編號
	
}
