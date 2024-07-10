package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class CourseOverviewCourseCategory {
	private Integer id;					//流水號
	private Integer parent_id;			//父層ID
	private String name;				//名稱
	private String english_name;		//英文名稱
	private String image;				//圖片
	private String layer;				//層級
	private String code;				//層級編號
	private String ten_one;				//小類 階層
	private String ten_two;				//小類 階層
	private String ten_three_begin;		//小類 階層
	private String ten_three_end;		//小類 階層
	private String encrypt_url;			//加密ID
	private String course_name;			//課程名稱
	private String create_by;			//創建者
	private Timestamp create_date;		//創建時間
	private String update_by;			//編輯者
	private Timestamp update_date;		//編輯時間
	private List<Map<String, Object>> smallCategoryList;
	private List<Map<String, Object>> courseNameList;
}
