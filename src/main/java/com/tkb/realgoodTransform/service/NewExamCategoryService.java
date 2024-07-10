package com.tkb.realgoodTransform.service;

import java.util.List;
import java.util.Map;



import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.tkb.realgoodTransform.model.NewExamCategory;
import com.tkb.realgoodTransform.model.User;

import jakarta.servlet.http.HttpServletRequest;



public interface NewExamCategoryService {
	public void index(NewExamCategory newExamCategory, Integer menu_id, Model model, HttpServletRequest request);
	
	/**
	 * 取得群組總筆數
	 * @param newExamCategory
	 * @return Integer
	 */
	public Integer getCount(NewExamCategory newExamCategory);

	/**
	 * 新增頁面
	 * @param newExamCategory
	 * @param model
	 * @param request
	 */
	public void addPage(NewExamCategory newExamCategory, Model model, HttpServletRequest request);

	/**
	 * 檢查類別是否重複
	 * @param newExamCategoryName
	 * @param layer
	 * @param request
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> checkNewExamCategory(String newExamCategoryName, Integer layer, HttpServletRequest request);

	/**
	 * 新增
	 * @param newExamCategory
	 * @param user
	 * @param model
	 */
	public void add(NewExamCategory newExamCategory, User user, Model model);

	/**
	 * 刪除前檢查是否有子清單
	 * @param checkList
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> checkData(String checkList);
	
	/**
	 * 取得子層類別資料清單
	 * @param newExamCategory
	 * @return List<NewExamCategory>
	 */
	public List<NewExamCategory> getSubList(NewExamCategory newExamCategory);

	/**
	 * 刪除類別
	 * @param deleteList
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> delete(String deleteList,User user);

	/**
	 * 重新排序資料
	 * @param parent_id
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> resetSort(Integer parent_id,User user);

	/**
	 * 修改頁面
	 * @param id
	 * @param model
	 */
	public void updatePage(Integer id, Model model);

	/**
	 * 修改類別
	 * @param newExamCategory
	 * @param user 
	 * @param model
	 */
	public void updateSubmit(NewExamCategory newExamCategory, User user, Model model);

	/**
	 * 取得類別資料清單(層級)
	 * @param newExamCategory
	 * @return List<NewExamCategory>
	 */
	public List<NewExamCategory> getLayerList(String layer, NewExamCategory newExamCategory);
	
	
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
