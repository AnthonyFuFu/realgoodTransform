package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 考取金榜
 */
@Data
public class Admit {
	
	private Integer id;						//流水號
	private String title;					//標題
	private String banner_content_black;	//banner黑字區域
	private String banner_content_red;		//banner紅字區域
	private String total_list;				//總表資料
	private String total_title;				//總表標題
	private String total_content;			//總表內文
	private String file_url;				//匯入的EXCEL檔存檔路徑
	private String create_by;				//創建者
	private Timestamp create_date;			//創建時間
	private String update_by;				//編輯者
	private Timestamp update_date;			//編輯時間
	private Integer click_rate;				//點擊率
	private Integer admit_year;				//榜單年度
	private Integer admit_category;			//榜單類別
	private String image_url;				//小圖路徑
	private String banner_url;				//banner路徑
	private String display;					//0:不顯示/1:顯示
	private String seo_content;				//SEO描述
	
	//暫存
	private String encrypt_id;				//加密id字串
	private String encrypturl;             //內頁URL
	private String content_title;			//標題
	private String content_summary;			//簡介
	private String content_achievement;		//名次
	private String content_name;			//名稱
	
	private Admit next_admit;				//下一筆金榜
	private Admit prev_admit;				//上一筆金榜
	
	private String category_name;			//分類中文
	private String show_message;			//頁面訊息
	
}
