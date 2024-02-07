package com.tkb.realgoodTransform.model;


import java.sql.Date;

import lombok.Data;

/**
 * 群組管理
 * 對照table: group_action
 * @author Joshua
 * @version 創建時間2022-03-03
 */
@Data
public class GroupAction {
	
	private Integer id;							//流水號
	private Integer group_id;					//參考groups.ID
	private Integer aciton_id;					//參考menu.ID
	private String create_by;					//創建者
	private Date create_date;					//創建日期
	private String update_by;					//編輯者
	private Date update_date;					//編輯日期
	
}
