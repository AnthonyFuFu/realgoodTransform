package com.tkb.realgoodTransform.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;



import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.tkb.realgoodTransform.model.CourseDiscount;
import com.tkb.realgoodTransform.model.CourseDiscountCategory;
import com.tkb.realgoodTransform.model.User;

import jakarta.servlet.http.HttpServletRequest;



public interface CourseDiscountService {
	/**
	 * 取得課程優惠資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param courseDiscount
	 * @return List<CourseDiscount>
	 */
	public List<CourseDiscount> getList(int pageCount, int pageStart, CourseDiscount courseDiscount);
	
	/**
	 * 取得前台課程優惠清單(依地區類別查詢)
	 * @param pageCount
	 * @param pageStart
	 * @param courseDiscount 
	 * @return List<CourseDiscount>
	 */
	public List<CourseDiscount> getFrontList(CourseDiscount courseDiscount);
	
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
	 * 取得前台單筆課程優惠
	 * @param courseDiscount
	 * @return CourseDiscount
	 */
	public CourseDiscount getFrontData(CourseDiscount courseDiscount);
	
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
	 * 修改課程優惠點擊率
	 * @param newExam
	 */
	public void updateClickRate(CourseDiscount courseDiscount);
	
	/**
	 * 刪除課程優惠
	 * @param id
	 */
	public void delete(CourseDiscount courseDiscount, Integer id);		
	
	/**
	 * 置頂次數
	 */
	public Integer checkTopCount(CourseDiscount courseDiscount);		
	/**
	 * 修改首頁排序
	 * @param newExam
	 */
    public void updateIndex_Sort(CourseDiscount courseDiscount);
    /**
	 * 新增頁面商業邏輯
	 * @param courseDiscountCategory
	 */
    public List<CourseDiscountCategory> addfunction(CourseDiscountCategory courseDiscountCategory);
    /**
	 * 新增功能商業邏輯
	 */
    public CourseDiscount addSubmitFunction(CourseDiscount courseDiscount, MultipartFile image, MultipartFile photo, User user)throws IOException;
    /**
   	 * 修改頁面商業邏輯
   	 * @param courseDiscountCategory
   	 */
    public Map<String, Object>updateFunction(CourseDiscount courseDiscount, CourseDiscountCategory courseDiscountCategory);
    /**
  	 * 檢查首頁置頂次數商業邏輯
  	 */
    public ResponseEntity<?> checkTopCountFunction(CourseDiscount courseDiscount)throws IOException;
    /**
   	 * 修改功能商業邏輯
   	 */
    public  void updateSubmitFunction(CourseDiscount courseDiscount, MultipartFile image, MultipartFile photo, User user,HttpServletRequest request) throws IOException;
    
    
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
