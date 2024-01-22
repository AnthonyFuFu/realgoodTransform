package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CourseDiscountReceive {

	private Integer id;						//收單ID
	private String name;					//填單人員
	private String sex;						//性別
	private String birthday;				//生日
	private String phone;					//電話
	private String cellphone;				//手機
	private String mail1;					//信箱1
	private String mail2;					//信箱2
	private String address;					//地址
	private String antecedent;				//學歷
	private String project;					//講座ID
	private String place;					//地點
	private String ps;						//備註
	private String schhool_department;		//學校
	private String showings;				//場次
	private String po_name;					//PO版人名稱
	private Timestamp from_time;			//填表時間
	private String area;					//地點
	private String take;					//備註2
	private String project_name;			//專案名稱
	private String accept;					//收單狀態
	private String ip_address;				//接收人
	private Timestamp take_time;			//接收時間
	private String admin_ps;				//後台備註
	
}
