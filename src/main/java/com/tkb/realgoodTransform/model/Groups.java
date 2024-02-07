package com.tkb.realgoodTransform.model;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;

/**
 * 群組管理
 */
@Data
@ToString
@Component
public class Groups {
	
	private Integer id;							//流水號
	private String name;						//群組名
	private String description;					//描述
	private String create_by;					//創建者
	private Date create_date;					//創建日期
	private String update_by;					//編輯者
	private Date update_date;					//編輯日期
	
	private String menu_list;

}
