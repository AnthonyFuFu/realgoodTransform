package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 群組
 */
@Data
public class Groups {

	private int id;
	private String name;
	private String description;
	private String action_list;
	private String user_list;
	private String create_by;
	private Timestamp create_date;
	private String update_by;
	private Timestamp update_date;
	
	private String account;
	
}
