package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 後台會員
 * 對照table: user_account
 * @author Joshua
 * @version 創建時間2022-01-24
 */
@Data
@ToString
public class User {
	
	private String password;// login用
	
	private Integer id;								//流水號，給group_user.USER_ID參考
	private String account;							//帳號
	private String chinese_name;					//姓名
	private String department_no;					//部門代號
	private String department_name;					//部門代號
	private String unit_no;							//單位代號
	private String unit_name;							//單位代號
	private String email;							//信箱
	private String status;							//狀態(0:關閉，1:開啟)
	private String create_by;						//創建者
	private Date create_date;						//創建日期
	private String update_by;						//編輯者
	private Date update_date;						//編輯日期
	private String area;							//館別代號
	private String location_no;							//館別代號
	
	private String company;//TKB
	/*搜尋用*/
	private String search_department_id;
	private String search_unit_id;
	private String search_employee_no;
	private String search_employee_name;
	private String search_email;
	
	private String area_name;
	private Integer group_id;
	private String groups_name;

}
