package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.CourseDiscountCategory;
import com.tkb.realgoodTransform.model.User;

import jakarta.servlet.http.HttpServletRequest;





public interface CourseDiscountCategoryService {
	/**
	 * 取得子層類別資料清單
	 * @param courseDiscountCategory
	 * @return List<CourseDiscountCategory>
	 */
	public List<CourseDiscountCategory> getSubList(CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 取得類別資料清單(層級)
	 * @param courseDiscountCategory
	 * @return List<CourseDiscountCategory>
	 */
	public List<CourseDiscountCategory> getLayerList(String layer, CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 取得類別資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param courseDiscountCategory
	 * @return List<CourseDiscountCategory>
	 */
	public List<CourseDiscountCategory> getList(int pageCount, int pageStart, CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 取得類別總筆數
	 * @param courseDiscountCategory
	 * @return Integer
	 */
	public Integer getCount(CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 取得單筆類別
	 * @param courseDiscountCategory
	 * @return CourseDiscountCategory
	 */
	public CourseDiscountCategory getData(CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增類別
	 * @param courseDiscountCategory
	 */
	public void add(CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 修改類別
	 * @param courseDiscountCategory
	 */
	public void update(CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 刪除類別
	 * @param id
	 */
	public void delete(CourseDiscountCategory courseDiscountCategory, Integer id);	
	
	/**
	 * 檢查類別是否重複
	 */
	public String checkCourseDiscountCategory(CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 重新排序
	 */
	public void resetSort(CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 檢查分類底下是否有還有活動
	 */
	public Integer deleteCheck(CourseDiscountCategory courseDiscountCategory);
	
	/**
	 * 新增功能商業邏輯
	 */
	public void addSubmitFunction(CourseDiscountCategory courseDiscountCategory, User user);
	/**
	 * 檢查重複類別名稱商業邏輯
	 */
	public String checkCourseDiscountCategoryFunction(CourseDiscountCategory courseDiscountCategory,HttpServletRequest request);
	/**
	 * 修改頁面商業邏輯
	 */
	public CourseDiscountCategory updateFunction(CourseDiscountCategory courseDiscountCategory);
	/**
	 * 修改功能商業邏輯
	 */
	public void updateSubmitFunction(CourseDiscountCategory courseDiscountCategory,User user);
	/**
	 * 檢查是否有子類別類別商業邏輯
	 */
	public String checkDataFunction(CourseDiscountCategory courseDiscountCategory,String deleteList);
	
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(CourseDiscountCategory courseDiscountCategory);
}
