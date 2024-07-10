package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.tkb.realgoodTransform.model.GroupAction;
import com.tkb.realgoodTransform.model.GroupUser;
import com.tkb.realgoodTransform.model.Groups;
import com.tkb.realgoodTransform.model.User;

import jakarta.servlet.http.HttpServletRequest;




public interface GroupsService {
	
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
	 * @param groups, request
	 */
	public void delete(Groups groups, HttpServletRequest request,GroupAction groupAction, User user);
	
	/**
	 * 取得群組資料清單(分頁)
	 * @param groups
	 * @return List<Groups>
	 */
	public List<Groups> getGroupsList();
	
	/**
	 * 取得修改頁面資訊
	 * @param groups, groupAction, model, request
	 */
	public void getUpdatePageData(Groups groups, GroupAction groupAction, Model model, HttpServletRequest request);
	
	/**
	 * 更新群組
	 * @param menu_list, groupAction, user, menu_list
	 */
	public void updateSubmit(Groups groups, GroupAction groupAction, User user, JSONArray menu_list, Model model);
	
	/**
	 * 新增群組
	 * @param groups, groupAction, menu_list, user
	 */
	public void addSubmit(Groups groups, GroupAction groupAction, JSONArray menu_list, User user, Model model);
	
	/**
	 * 檢查群組內是否還有員工還有員工
	 * @param request 
	 * @param groupUser 
	 */
	public ResponseEntity<?> checkData(GroupUser groupUser, HttpServletRequest request);
	
	/**
	 * 透過group名稱找出group_id
	 * @return Integer
	 */
	public Integer getGroupId(String group_name);
	
	
	/**
	 * 檢查群組名稱是否重複
	 */
	public List<Map<String,Object>> judegeRepeat(String group_name);
	
	/**
	 * 取得群組Id
	 * @param groups
	 * @return Integer
	 */
	public Integer getGroupId(User user);

}
