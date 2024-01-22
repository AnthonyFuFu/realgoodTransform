package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UserLoginLog {

	private Integer id;
	private String account;
	private String ip;
	private String status;
	private Timestamp create_date;
	
}
