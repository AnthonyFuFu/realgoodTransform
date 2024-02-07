package com.tkb.realgoodTransform.model;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群組管理
 * 對照table: group_user
 * @author Joshua
 * @version 創建時間2022-03-30
 */
@Data
@NoArgsConstructor
public class GroupUser {
	private Integer id;							//流水號
	private Integer group_id;					//參考groups.ID
	private Integer user_id;					//參考user_account.ID
	private String create_by;					//創建者
	private Date create_date;					//創建日期
	private String update_by;					//編輯者
	private Date update_date;					//編輯日期
}
