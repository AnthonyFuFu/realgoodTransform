package com.tkb.realgoodTransform.model;

import java.util.Date;

import lombok.Data;

@Data
public class UserLoginLog {

	private Integer id;
	private String account;
	private String ip;
	private String status;
	private Date create_date;
	
}
