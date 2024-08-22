package com.tkb.realgoodTransform.dao;

import java.util.List;
import java.util.Map;

import com.tkb.realgoodTransform.model.CourseDiscountContent;

public interface CourseDiscountContentDao {

	/**
	 * 取得課程優惠內容資料清單
	 * @param courseDiscountContent
	 * @return List<CourseDiscountContent>
	 */
	public List<CourseDiscountContent> getList(CourseDiscountContent courseDiscountContent);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增課程優惠內容
	 * @param courseDiscountContent
	 */
	public void add(CourseDiscountContent courseDiscountContent);
	
	/**
	 * 修改課程優惠內容
	 * @param courseDiscountContent
	 */
	public void update(CourseDiscountContent courseDiscountContent);
	
	/**
	 * 刪除課程優惠內容
	 * @param id
	 */
	public void delete(Integer id);			

//	=======================
	public List<Map<String, Object>> getNormalList();
	public void updateNormalData(CourseDiscountContent courseDiscountContent);
}
