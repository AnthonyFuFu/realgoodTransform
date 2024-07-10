package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.CourseDiscount;



public interface CourseDiscountDao {
	/**
	 * 取得課程優惠資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param courseDiscount
	 * @return List<CourseDiscount>
	 */
	public List<CourseDiscount> getList(int pageCount, int pageStart, CourseDiscount courseDiscount);
	
	
	/**
	 * 取得前台課程優惠資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param courseDiscount
	 * @return List<CourseDiscount>
	 */
	public List<CourseDiscount> getFrontList(int pageCount, int pageStart, CourseDiscount courseDiscount, String search_sort);
	
	/**
	 * 取得前台課程優惠資料清單(首頁專用，限制5筆)
	 * @return List<CourseDiscount>
	 */
	public List<CourseDiscount> getFrontList();
	
	/**
	 * 取得課程優惠總筆數
	 * @param courseDiscount
	 * @return Integer
	 */
	public Integer getCount(CourseDiscount courseDiscount);
	
	/**
	 * 取得前台課程優惠總筆數
	 * @param courseDiscount
	 * @return Integer
	 */
	public Integer getFrontCount(CourseDiscount courseDiscount);

	/**
	 * 取得單筆課程優惠
	 * @param courseDiscount
	 * @return CourseDiscount
	 */
	public CourseDiscount getData(CourseDiscount courseDiscount);
	
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增課程優惠
	 * @param newExam
	 */
	public void add(CourseDiscount courseDiscount);
	
	/**
	 * 修改課程優惠
	 * @param newExam
	 */
	public void update(CourseDiscount courseDiscount);
	
	
	/**
	 * 刪除課程優惠
	 * @param id
	 */
	public void delete(CourseDiscount courseDiscount,Integer id);		
	
	/**
	 * 置頂次數
	 */
	public Integer checkTopCount(CourseDiscount courseDiscount);
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(CourseDiscount courseDiscount);
}
