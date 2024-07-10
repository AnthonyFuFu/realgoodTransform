package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.NewExam;



public interface NewExamDao {

	/**
	 * 取得最新考情資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param newExam
	 * @return List<NewExam>
	 */
	public List<NewExam> getList(int pageCount, int pageStart, NewExam newExam);
	
	/**
	 * 取得前台最新考情清單(依地區類別查詢)
	 * @param pageCount
	 * @param pageStart
	 * @param newExam 
	 * @return List<NewExam>
	 */
	public List<NewExam> getFrontList(NewExam newExam);
	
	/**
	 * 取得前台最新考情資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param newExam
	 * @return List<NewExam>
	 */
	public List<NewExam> getFrontList(int pageCount, int pageStart, NewExam newExam, String search_sort);
	
	/**
	 * 取得前台最新考情資料清單(地區專用)
	 * @param pageCount
	 * @param pageStart
	 * @param newExam
	 * @param newExamList
	 * @return List<NewExam>
	 */
	public List<NewExam> getFrontList(int pageCount, int pageStart, NewExam newExam, List<Area> areaList, String search_sort);
	
	/**
	 * 取得前台最新考情資料清單(首頁專用，限制5筆)
	 * @return List<NewExam>
	 */
	public List<NewExam> getFrontList();
	
	/**
	 * 取得最新考情總筆數
	 * @param newExam
	 * @return Integer
	 */
	public Integer getCount(NewExam newExam);
	
	/**
	 * 取得前台最新考情總筆數
	 * @param newExam
	 * @return Integer
	 */
	public Integer getFrontCount(NewExam newExam);
	
	/**
	 * 取得前台最新考情總筆數(地區專用)
	 * @param newExam
	 * @param areaList
	 * @return
	 */
	public Integer getFrontCount(NewExam newExam, List<Area> areaList);
	
	/**
	 * 取得單筆最新考情
	 * @param newExam
	 * @return NewExam
	 */
	public NewExam getData(NewExam newExam);
	
	/**
	 * 取得前台單筆最新考情
	 * @param newExam
	 * @return NewExam
	 */
	public NewExam getFrontData(NewExam newExam);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增最新考情
	 * @param newExam
	 */
	public void add(NewExam newExam);
	
	/**
	 * 修改最新考情
	 * @param newExam
	 */
	public void update(NewExam newExam);
	
	/**
	 * 修改最新考情點擊率
	 * @param newExam
	 */
	public void updateClickRate(NewExam newExam);
	
	/**
	 * 刪除最新考情
	 * @param id
	 */
	public void delete(NewExam newExam,Integer id);
	
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(NewExam newExam);
	
}
