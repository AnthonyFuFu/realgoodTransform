package com.tkb.realgoodTransform.dao;

import java.util.List;

import com.tkb.realgoodTransform.model.GroupAction;



public interface GroupActionDao {
	
	/**
	 * 新增群組
	 * @param groupAction
	 */
	public void add(GroupAction groupAction);
	
	/**
	 * 取得單筆群組
	 * @param GroupAction
	 * @return List<GroupAction>
	 */
	public List<GroupAction> getData(GroupAction groupAction);
	
	/**
	 * 刪除群組權限(先刪除)
	 * @param GroupAction
	 */
	public void delete(GroupAction groupAction);
	
	/**
	 * 更新群組權限(後更新)
	 * @param GroupAction
	 */
	public void update(GroupAction groupAction);

}
