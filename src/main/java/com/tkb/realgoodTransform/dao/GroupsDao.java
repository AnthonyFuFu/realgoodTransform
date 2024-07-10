package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Groups;
import com.tkb.realgoodTransform.model.User;




public interface GroupsDao {
	
	/**
	 * 取得群組總筆數
	 * @param groups
	 * @return Integer
	 */
	public Integer getCount(Groups groups);
	
	/**
	 * 取得群組資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param groups
	 * @return List<Groups>
	 */
	public List<Groups> getList(int pageCount, int pageStart, Groups groups);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增群組
	 * @param groups
	 */
	public void add(Groups groups);
	
	/**
	 * 取得群組資訊
	 * @return Groups
	 * @param groups
	 */
	public Groups getGroup(Groups groups);
	
	/**
	 * 刪除群組
	 * @param id
	 */
	public void delete(Groups groups);
	
	/**
	 * 取得群組資料清單(分頁)
	 * @param groups
	 * @return List<Groups>
	 */
	public List<Groups> getGroupsList();
	
	public void update(Groups groups);
	
	/**
	 * 透過group名稱找出group_id
	 * @return Integer
	 */
	public Integer getGroupId(String group_name);
	
	
	/**
	 * 檢查群組名稱是否重複
	 */
	public List<Map<String,Object>> judegeRepeat(String group_name);
	
	
	public Integer getGroupId(User user);

}
