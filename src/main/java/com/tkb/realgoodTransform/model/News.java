package com.tkb.realgoodTransform.model;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 最新消息
 */
@Data
public class News {

	private int id;
	private String title;
	private String content;
	private String image;
	private int sort;
	private String create_by;
	private String update_by;
	private Timestamp create_date;
	private Timestamp update_date;
	
}
