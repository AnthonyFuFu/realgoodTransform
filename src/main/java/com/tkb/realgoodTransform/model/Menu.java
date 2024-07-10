package com.tkb.realgoodTransform.model;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 後台會員
 * 對照table: menu
 * @author Joshua
 * @version 創建時間2022-01-24
 */
@Getter
@Setter
@ToString
@Component
public class Menu {
	
	private Integer id;								//流水號，給group_action.ACTION_ID參考
	private Integer parent_id;						//父層ID
	private String name;							//名稱
	private String layer;							//層級
	private String link;							//連結
	private String create_by;						//創建者
	private Date create_date;						//創建日期
	private String update_by;						//編輯者
	private Date update_date;						//編輯日期
	
	private String parent_name;						//父層名稱
	private List<Menu> menu_list;

}
