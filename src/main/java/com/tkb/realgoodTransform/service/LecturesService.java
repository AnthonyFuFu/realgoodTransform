package com.tkb.realgoodTransform.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;



import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.tkb.realgoodTransform.model.Area;
import com.tkb.realgoodTransform.model.Lectures;
import com.tkb.realgoodTransform.model.LecturesCategory;
import com.tkb.realgoodTransform.model.LecturesContent;
import com.tkb.realgoodTransform.model.LecturesPlace;
import com.tkb.realgoodTransform.model.User;

import jakarta.servlet.http.HttpServletRequest;



public interface LecturesService {
	/**
	 * 取得熱門講座資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param lectures
	 * @return List<Lectures>
	 */
	public List<Lectures> getList(int pageCount, int pageStart, Lectures lectures);
	
	/**
	 * 取得前台熱門講座清單(依地區類別查詢)
	 * @param pageCount
	 * @param pageStart
	 * @param lectures
	 * @return List<Lectures>
	 */
	public List<Lectures> getFrontList(Lectures lectures);
	
	/**
	 * 取得前台熱門講座資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param lectures
	 * @return List<Lectures>
	 */
	public List<Lectures> getFrontList(int pageCount, int pageStart, Lectures lectures, String search_sort);

	/**
	 * 取得前台熱門講座資料清單(分頁)
	 * @param pageCount
	 * @param pageStart
	 * @param lectures
	 * @return List<Lectures>
	 */
	public List<Lectures> getIndexFrontList(int pageCount, int pageStart, Lectures lectures, String search_sort);
	
	/**
	 * 取得前台熱門講座資料清單(地區專用)
	 * @param pageCount
	 * @param pageStart
	 * @param lectures
	 * @param lecturesList
	 * @return List<Lectures>
	 */
	public List<Lectures> getFrontList(int pageCount, int pageStart, Lectures lectures, List<Area> areaList, String search_sort);
	
	/**
	 * 取得熱門講座總筆數
	 * @param lectures
	 * @return Integer
	 */
	public Integer getCount(Lectures lectures);
	
	/**
	 * 取得前台百官網公告總筆數
	 * @param lectures
	 * @return Integer
	 */
	public Integer getFrontCount(Lectures lectures);
	
	/**
	 * 取得前台熱門講座總筆數(地區專用)
	 * @param lectures
	 * @param areaList
	 * @return
	 */
	public Integer getFrontCount(Lectures lectures, List<Area> areaList);
	
	/**
	 * 取得單筆熱門講座
	 * @param lectures
	 * @return lectures
	 */
	public Lectures getData(Lectures lectures);
	
	/**
	 * 取得前台單筆熱門講座
	 * @param lectures
	 * @return lectures
	 */
	public Lectures getFrontData(Lectures lectures);
	
	/**
	 * 取得下一筆ID
	 * @return Integer
	 */
	public Integer getNextId();
	
	/**
	 * 新增熱門講座
	 * @param lectures
	 */
	public void add(Lectures lectures);
	
	/**
	 * 修改熱門講座
	 * @param lectures
	 */
	public void update(Lectures lectures);
	
	/**
	 * 修改熱門講座點擊率
	 * @param lectures
	 */
	public void updateClickRate(Lectures lectures);
	
	/**
	 * 刪除熱門講座
	 * @param id
	 */
	public void delete(Integer id);	
	
	/**
	 * 置頂次數
	 */
	public Integer checkTopCount(Lectures lectures);
	
	/**
     * 取得熱門講座資料清單
     * @param lectures
     * @return List<Lectures>
     */
	public List<Lectures> getList(Lectures lectures);
	
   /**
    * 修改熱門講座
    * @param lectures
    */
	public void updateId(Lectures lectures);
	
	/**
	 * 新增頁面
	 */
	public List<LecturesCategory>add(LecturesCategory lecturesCategory, LecturesPlace lecturesPlace,Lectures lectures);
	
	/**
	 * 新增資料
	 */
	public void addSubmit(Lectures lectures, LecturesContent lecturesContent, LecturesPlace lecturesPlace, User user, HttpServletRequest request, Model model);
	
	/**
	 * 修改頁面
	 */
	public Map<String, Object>update(Lectures lectures, LecturesCategory lecturesCategory, LecturesContent lecturesContent, LecturesPlace lecturesPlace);
	
	/**
	 * 修改資料
	 */
	public void updateSubmit(Lectures lectures, LecturesContent lecturesContent, LecturesPlace lecturesPlace, User user, HttpServletRequest request, Model model);

	/**
	 * 刪除百官網公告
	 */
	public void delete(Lectures lectures, LecturesContent lecturesContent, LecturesPlace lecturesPlace, HttpServletRequest request, User user);

	/**
	 * 檢查置頂次數
	 */
	public ResponseEntity<?>checkTopCountFunction(Lectures lectures)throws IOException;
	
	/**
	 * 取得第二層類別
	 */
	public ResponseEntity<?> changeCategory(String id , LecturesCategory lecturesCategory);

	/**
	 * 取得免費課程資料
	 */
	public List<Lectures> getFreeCourse();

	public List<Map<String, Object>> getPlaceName(Integer id);
	
//	=======================
	/**
	 * 獲取正是機list
	 */
	public List<Map<String, Object>> getNormalList();
	/**
	 * 更新增增正是機Banner
	 * @param Banner
	 */
	public void updateNormalData(Lectures lectures);
	
	
}
