package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Authority {

	private int id;						//流水號
	private int group_id;				//群組編號
	private String action;				//功能
	private String added;				//新增
	private String remove;				//刪除
	private String amend;				//修改
	private String inquire;				//查詢
	private String browse;				//瀏覽
	private String sort;				//排序
	private String create_group;		//建立群組
	private String account_manage;		//帳號管理
	private String top_mangagement;		//最高管理者
	private String create_by;			//創建者
	private Timestamp create_date;		//創建日期
	private String update_by;			//編輯者
	private Timestamp update_date;		//編輯日期
	
	private String groups_name;			//群組中文
	private String menu_name;			//選單中文
	
}
