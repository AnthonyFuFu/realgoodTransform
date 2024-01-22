package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 後台會員
 */
@Data
public class UserAccount {
	
	private int id;
	private String account;
	private String password;
	private String chinese_name;
	private String department_no;
	private String unit_no;
	private String email;
	private String groups;
	private String status;
	private String create_by;
	private String update_by;
	private Timestamp create_date;
	private Timestamp update_date;
	private String area;
	
	private String company;
	private String dept_name;
	private String area_name;
	private String groups_name;
	
}
