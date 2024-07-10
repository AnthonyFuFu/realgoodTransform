package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.NewExamContent;



public interface NewExamContentDao {
	/**
	 * 取得最新考情內容資料清單
	 * @param newExamContent
	 * @return List<NewExamContent>
	 */
	public List<NewExamContent> getList(NewExamContent newExamContent);

	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增最新考情內容
	 * @param newExamContent
	 */
	public void add(NewExamContent newExamContent);
	
	/**
	 * 修改最新考情內容
	 * @param newExamContent
	 */
	public void update(NewExamContent newExamContent);
	
	/**
	 * 刪除最新考情內容
	 * @param id
	 */
	public void delete(Integer id);
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(NewExamContent newExamContent);
}
