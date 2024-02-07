package com.tkb.realgoodTransform.model;

import java.util.Date;

import lombok.Data;

/**
 * 後台會員
 */
@Data
//TkpApi用
public class UserAccount {

	private String account;	// login用
	private String password;// login用

	private Integer user_account_id;
	private String employee_no;
	private String employee_name;
	private String unit_id;
	private String department_id;
	private String business_group_id;
	private String email;
	private String status;
	private Integer group_id;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String check_date;
	
	private String department_no;//部門
	private String company;//TKB
	
	/*搜尋用*/
	private String search_department_id;
	private String search_unit_id;
	private String search_employee_no;
	private String search_employee_name;
	private String search_email;
	
	// 舊的
	private Integer id;
	private String chinese_name;
	private String unit_no;
	private String area;
	private String dept_name;
	private String area_name;
	private String groups_name;
	
}
