package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.NewExamCategory;



public interface NewExamCategoryDao {
	
	/**
	 * 取得群組總筆數
	 * @param newExamCategory
	 * @return Integer
	 */
	public Integer getCount(NewExamCategory newExamCategory);
	
	/**
	 * 檢查類別是否重複
	 * @param newExamCategory
	 * @return NewExamCategory
	 */
	public List<NewExamCategory> checkNewExamCategory(NewExamCategory newExamCategory);

	/**
	 * 新增類別
	 * @param newExamCategory
	 */
	public void add(NewExamCategory newExamCategory);

	/**
	 * 取得資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param newExamCategory
	 * @return
	 */
	public List<NewExamCategory> getList(int pageCount, int pageStart, NewExamCategory newExamCategory);
	
	/**
	 * 取得子層類別資料清單
	 * @param newExamCategory
	 * @return List<NewExamCategory>
	 */
	public List<NewExamCategory> getSubList(NewExamCategory newExamCategory);

	/**
	 * 刪除類別
	 * @param newExamCategory
	 */
	public void delete(NewExamCategory newExamCategory);
	
	/**
	 * 重新排序資料
	 * @param parent_id
	 */
	public void resetSort(Integer parent_id);
	
	/**
	 * 取得單筆類別
	 * @param id
	 * @return NewExamCategory
	 */
	public NewExamCategory getData(Integer id);

	/**
	 * 修改類別
	 * @param newExamCategory
	 */
	public void update(NewExamCategory newExamCategory);
	
	/**
	 * 取得類別資料清單(層級)
	 * @param newExamCategory
	 * @return List<NewExamCategory>
	 */
	public List<NewExamCategory> getLayerList(String layer, NewExamCategory newExamCategory);
	
	public Integer getNextId();
	
	public Integer deleteCheck(NewExamCategory newExamCategory);
	
	
	/**
	 * 取得最新考情資資料
	 * @param newExamCategory
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getNewExamMenuMenu(NewExamCategory newExamCategory);
	
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(NewExamCategory newExamCategory);

}
