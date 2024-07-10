package com.tkb.realgoodTransform.dao;

import java.util.List;

import com.tkb.realgoodTransform.model.GroupUser;



public interface GroupUserDao {

	/**
	 * 新增使用者群組(group_user)
	 * @param groupUser
	 */
	public void add(GroupUser groupUser);
	
	/**
	 * 更新使用者群組(group_user)
	 * @param groupUser
	 */
	public void update(GroupUser groupUser);
	
	/**
	 * 檢查使用者是否已有權限群組
	 * @param groupUser
	 */
	public Integer checkUser(GroupUser groupUser);
	
	/**
	 * 取得使用者群組id
	 * @param id
	 */
	public Integer getGroupId(Integer id);
	
	/**
	 * 取得單筆群組
	 * @param GroupAction
	 * @return List<GroupAction>
	 */
	public List<GroupUser> getData(GroupUser groupUser);
	
	
	/**
	 * 給排程更新用的
	 */
	public void insertAndUpdateGroupsUser(GroupUser groupUser);
	
}
